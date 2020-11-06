package com.kuaidaoresume.matching.service.helper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ChineseHelperTest {

    @Autowired
    private ChineseHelper chineseHelper;

    @Test
    public void test_containsChinese_noChinese() {
        assertFalse(chineseHelper.containsChinese("English only"));
    }

    @Test
    public void test_containsChinese_chineseOnly() {
        assertTrue(chineseHelper.containsChinese("纯中文"));
    }

    @Test
    public void test_containsChinese_chineseAndEnglish() {
        assertTrue(chineseHelper.containsChinese("Both 中文 and English"));
    }

    @Test
    public void test_convertChineseTermForSearch_chineseOnly() {
        assertThat(chineseHelper.convertChineseForSearch("医药三终端销售经理"), is("医药 三 终端 销售 经理"));
    }

    @Test
    public void test_convertChineseTermForSearch_chineseAndEnglish() {
        assertThat(chineseHelper.convertChineseForSearch("JW Lounge Assistant Manager 万豪酒廊副经理"),
            is("JW Lounge Assistant Manager 万豪 酒廊 副经理"));
    }

    @Test
    public void test_convertChineseTermForSearch_withDashAndSlash() {
        assertThat(chineseHelper.convertChineseForSearch("口腔医生/正畸-儿牙-种植-修复-综合（上市外企 五险一金）"),
            is("口腔 医生 正畸 儿牙 种植 修复 综合 上市 外企 五险 一金"));
    }

    @Test
    public void test_convertChineseTermForSearch_withComma() {
        assertThat(chineseHelper.convertChineseForSearch("法规事务总监/副总裁 VP / Director, Regulatory Affairs"),
            is("法规 事务 总监 副总裁 VP Director Regulatory Affairs"));
    }

    @Test
    public void test_convertChineseTermForSearch_withDigit() {
        assertThat(chineseHelper.convertChineseForSearch("《70万顶薪》高级UI设计师0200 (MJ000190)"),
            is("70 万顶薪 高级 UI 设计师 0200 MJ000190"));
    }

    @Test
    public void test_convertChineseTermForSearch_withSquareBracket() {
        assertThat(chineseHelper.convertChineseForSearch("[上海天际人才服务有限公司]生产管理"),
            is("上海 天际 人才 服务 有限公司 生产 管理"));
    }

    @Test
    public void test_convertChineseTermForSearch_withColon() {
        assertThat(chineseHelper.convertChineseForSearch("厂长/生产总监（工作地点：东莞长安）"),
            is("厂长 生产 总监 工作 地点 东莞 长安"));
    }
}
