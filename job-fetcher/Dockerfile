FROM python:3.8

WORKDIR /code

COPY requirements.txt .

COPY config.json .

COPY mock_job_results.json .

COPY JobFetcher.py .

COPY LocationClassifier.py .

COPY locationData/ locationData/

RUN pip install -r requirements.txt

ENTRYPOINT ["python", "JobFetcher.py"]