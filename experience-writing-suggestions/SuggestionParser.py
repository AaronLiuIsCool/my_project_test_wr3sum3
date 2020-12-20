#!/usr/bin/env python
# -*- coding: UTF-8 -*-
# pip install pylightxl==1.51

import pylightxl as xl


class SuggestionParser:
    def __init__(self, file):
        self.db = xl.readxl(fn=file)

    def generate_sql_script(self, output_file, sheet='Content'):
        industry, position, sample_sentence, keywords = '', '', '', ''
        rows = self.db.ws(ws=sheet).rows
        next(rows)  # skip the first header row
        statements = []

        def convert_number_to_text(val):
            if isinstance(val, float) and val < 1:
                return f'{val * 100}%'
            else:
                return str(val)

        for row in rows:
            if row[0]:
                industry = row[0].strip()
            if row[1]:
                position = row[1].strip()
            sample_sentence = row[2].strip()
            keywords = ','.join(filter(None, map(convert_number_to_text, row[3:])))
            statement = f'''INSERT INTO `suggestion` (`industry`, `position_title`, `texts`, `suggestion_keywords`) VALUES (
                '{industry}', 
                '{position}', 
                '{sample_sentence}', 
                '{keywords}'
            );\n'''
            statements.append(statement)

        out_file = open(output_file, 'w')
        out_file.writelines(statements)
        out_file.close()


if __name__ == "__main__":
    parser = SuggestionParser('suggestions/suggestions_en_v1.xlsx')
    parser.generate_sql_script('../job-svc/src/main/resources/db/migration/V2__populate_suggestions_en_v1.sql')
