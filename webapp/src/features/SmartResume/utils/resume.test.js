import {
  flatten,
  reconstruct,
  generateBasicFormRating,
  generateLayoutRating,
  generateSuggestions,
  generateEducationRatings,
  generateCertificeRating,
} from './resume';

const testObj = {
  a: 'ezio auditore',
  b: {
    c: 'nothing is true',
    d: {
      e: 'everything is permitted',
    },
  },
  f: {
    g: [
      'templars',
      'assassins',
      {
        h: 'bye',
      },
    ],
  },
};

const flattenTestObj = {
  a: 'ezio auditore',
  'b.c': 'nothing is true',
  'b.d.e': 'everything is permitted',
  'f.g.0': 'templars',
  'f.g.1': 'assassins',
  'f.g.2.h': 'bye',
};

const result = [
  {content: '■ asd', page: 1, type: 'list', y: 155.91666666666669},
];

const messages = {
  title: '智能创建',
  basicInfo: '基本信息',
  enterBasicInfo: '请输入您的一些基本个人信息',
  education: '教育背景',
  educationInfo: '请添加关于您教育背景的相关信息',
  workXp: '工作经历',
  projectXp: '项目经历',
  otherXp: '志愿者经历',
  certifications: '技能证书',
  writeAssistant: '经历写作助手',
  addNewEducationExperience: '添加教育背景',
  addNewCertificate: '添加证书',
  addNewExperience: '添加新经历',
  uploadPhoto: '上传照片',
  addPhoto: '添加头像',
  samples: '什么样的照片试用于简历?',
  cnName: '中文姓名',
  enterCnName: '请输入您的中文全名',
  enName: '英文名称',
  enterEnName: 'First & Last Name',
  email: '邮箱',
  enterEmail: '请填写您的邮箱地址',
  phone: '联系电话',
  enterPhone: '输入您的联系电话',
  city: '城市',
  enterCity: '您现在的居住城市',
  linkedin: '个人领英 Linkedin 链接',
  enterLinkedin: '请填写您的Linkedin链接',
  weblink: '个人 Github 或者作品集链接',
  enterWeblink: '请填写您的Github链接或作品集链接',
  resetForm: '清空重填',
  confirm: '确认',
  enterSchoolInfo: '填写院校信息',
  schoolName: '院校名称',
  enterSchoolName: '请输入学校名字',
  graduateGPA: '毕业GPA',
  enterSchoolDate: '入学日期',
  graduateDate: '毕业日期',
  major: '专业',
  enterMajor: '输入所在的专业',
  degree: '学历',
  enterDegree: '请输入您获得的学历',
  schoolCity: '所在城市',
  schoolCountry: '学校所在国家',
  highestAward: '所获最高奖项 (有任何奖项都是加分好事！)',
  enterHighestAward: '请填写您在校期间所获得的最高奖项',
  otherAward: '其他获得奖项',
  enterOtherAward: '请填写您在校期间所获得的其他奖项',
  save: '保存',
  entryIsInvalid: '请输入正确的信息',
  enterNewExperience: '填写新经历',
  work: '工作或实习经历',
  workInfo:
    '请您尽可能的表述您前10份的工作/实习/项目经历。从您最近经历开始说起。',
  yes: '是',
  no: '否',
  workName: '岗位名称',
  enterWorkName: '请填写您的岗位名称',
  stillAtWork: '是否已离职？',
  companyName: '单位/公司名称',
  enterCompanyName: '请填写您所在的单位/公司名称',
  workStartDate: '入职日期',
  workEndDate: '离职日期',
  workCity: '岗位所在城市',
  country: '国家',
  workCountry: '岗位所在国家',
  workDetailsDescription: '岗位细节描述',
  enterWorkDetailsDescription: '说说您在职的时所做的事情',
  project: '项目经历',
  projectInfo:
    '请您尽可能的表述您前10份的工作/实习/项目经历。从您最近经历开始说起。',
  participateRole: '项目名称',
  enterParticipateRole: '请填写所参与项目的名称',
  endOrNot: '是否已结束？',
  projectStartDate: '项目开始时间',
  projectEndDate: '项目结束时间',
  projectCity: '项目所在城市',
  projectCountry: '项目所在国家',
  projectDetailsDescription: '项目细节描述',
  enterProjectDetailsDescription: '参与身份',
  volunteerExperience: '志愿者经历',
  enterVolunteerExperience:
    '请您尽可能的表述您志愿者经历。从您最近经历开始说起。',
  leaveOrNot: '是否已离开？',
  institutionName: '机构名称',
  enterInstitutionName: '请填报名参与志愿者的机构的名称',
  volunteerStartDate: '志愿经历开始时间',
  volunteerEndDate: '志愿经历结束时间',
  experienceCity: '经历所在城市',
  experienceCountry: '经历所在国家',
  volunteerDetailsDescription: '志愿者经历细节描述',
  enterVolunteerDetailsDescription: '说说您在职的时所做的事情',
  certificate: '技能证书',
  certificateInfo: '请添加关于您教育背景的相关信息',
  certificateName: '证书名称',
  enterCertificateName: '请输入证书的名称',
  validForever: '永久有效？',
  issueDate: '颁发时间/获得时间',
  expireDate: '有效期至',
  yymmdd: '年/月/日期',
  dateDisplayFormat: 'YYYY/MM/DD',
  clickForDetails: '点击查看强度详情',
  score_A: '优秀',
  score_B: '良好',
  score_C: '及格',
  score_D: '薄弱',
  score_A_MSG: '简历强度:简历信息完整, 表达专业 牛人你真棒！',
  score_B_MSG: '简历强度:简历信息完整, 参考‘写作助手’专业术语继续提升吧！',
  score_C_MSG: '简历强度:及格, 简历信息有所缺失, 继续加油哦！',
  score_D_MSG: '简历强度:薄弱, 简历信息较少, 请前往补全更多信息！',
  RPreview: {
    linkedinLink: '领英链接',
    githubLink: 'Github链接',
    educationBackground: '教育背景',
    workExperience: '工作经历',
    projectExperience: '项目经历',
    studentWorkAndVolunteer: '志愿服务',
    certificateAndAward: '技能证书 & 获得荣誉',
    awards: '获得荣誉',
    certificate: '技能证书',
    validForever: '永久有效',
    expiredAt: '到期',
    current: '现今',
    editThemeColor: '编辑主题色',
    smartTranslation: '智能翻译',
    oneClickWholePage: '一键整页',
    downloadResume: '下载简历',
    perparingResume: '生成中..',
    downloadResume: '下载简历',
    linkedIn: '领英链接',
    weblink: '项目集链接',
  },
  resumeTips: {
    resumeTips: '简历编写小帖士',
  },
  photoMessage: '如果您投递中国地区的岗位，建议您添加个人简历照片',
  personalWebsite: '个人网站',
  websiteMessage: '填写个人网站会显得更专业哟！',
  linkedinAccount: '领英账号',
  linkedinMessage: '填写领英账号会显得更专业哟！',
  resume: '简历',
  notAlign: '页数不工整',
  layoutMessage:
    '您的简历，尚未进行整页排版，请使用<b>“一键整页”</b>让我们帮您自动优化排版！',
  highestAwards: '所获最高奖项',
  highestAwardsMessage: '填写所获最高奖项显得更专业哟！',
  otherAwardMessage: '填写其他获得奖项显得更专业哟！',
  gpaMessageOne: '您的GPA在3.5以上，建议您填写，当作一个亮点来展示',
  gpaMessageTwo: '您的GPA，在3.0以下，不建议您添加到简历上',
  gpaMessageThree: '您的GPA在3-3.5之间，您可以填写在简历上呢',
  schoolRating1: '是世界名校，非常棒哟！',
  schoolRating2: '知名度蛮高的，可以可以！',
  schoolRating3: '是很有特色的一所院校，还不错哟！',
  schoolPopularity: '学校知名度',
  good: '挺好',
  verygood: '很好',
  extremegood: '非常好',
  certificationsMessage:
    '技能证书，是非常重要的资质证明，您这块背景较为薄弱，建议您查看快课，快速掌握专业技能，提升背景竞争力',
  workXpLengthOverTwo:
    '您已经提供3+段工作经历，数量充实哟，建议您查看经历写作助手，提高表达专业性',
  workXpLengthTwo:
    '您已经提供2段工作经历，数量上，还是可以的，建议您查看经历写作助手，提高表达专业性',
  workXpLengthOne:
    '您目前只有1段工作经历，显得比较单薄，如果可以的话，建议您再添加一段相关经历，提高竞争力。',
  workXpLengthZero:
    '您目前没有工作经历，是一个明显软肋，如果可以的话，建议您再添加一段相关经历，提高竞争力。',
  projectXpLengthOverTwo:
    '您已经提供3+段专业项目经历，数量充实哟，建议您查看经历写作助手，提高表达专业性',
  projectXpLengthTwo:
    '您已经提供2段专业项目经历，数量上，还是可以的，建议您查看经历写作助手，提高表达专业性',
  projectXpLengthOne:
    '您目前只有1段专业项目经历，显得比较单薄，如果可以的话，建议您再添加一段相关经历，提高竞争力。',
  projectXpLengthZero:
    '您目前没有专业项目经历，如果可以的话，建议您再添加一段相关经历，提高竞争力。',
  otherXpLengthOverTwo:
    '您已经提供3+段志愿者经历，数量充实哟，建议您查看经历写作助手，提高表达专业性',
  otherXpLengthTwo:
    '志愿者经历，可以更加全面展示，专业技能之外的特质、能力，比如人际交往、活动策划等等，您目前只有2段志愿者经历，可以参考“经历写作助手”再多补充一些',
  otherXpLengthOne:
    '志愿者经历，可以更加全面展示，专业技能之外的特质、能力，比如人际交往、活动策划等等，您目前只有1段志愿者经历，可以参考“经历写作助手”再多补充一些',
  otherXpLengthZero:
    '志愿者经历，可以更加全面展示，专业技能之外的特质、能力，比如人际交往、活动策划等等，您目前没有志愿者经历，可以参考“经历写作助手”再多补充一些',
  workExp: '工作实习经历',
  projectExp: '项目经历',
  otherExp: '志愿者经历',
  sorted: '时间排序',
  sortedMessage: '您的经历模块，还未按时间排序，“一键排序”帮您自动排序',
  employedAt1: '您曾经就职的单位',
  employedAt2: '，是行业',
  employedAt3: '龙头之一',
  employedAt4: '明星企业',
  employedAt5: '，这段经历含金量很棒哟！',
  nationalInfluence: '就业于全球知名单位！',
  globalInfluence: '就业于全国知名单位！',
  graduatedFrom: '您的毕业院校{school}，',
  ratingDetail: '简历强度分析详情',
  notEnter: '您没有填写',
  jumpTo: '前往',
  expVal: '经历含金量',
  tooLess: '数量过少',
  entireLayout: '简历整体版式',
  all: '全部',
  expLackOf: '经历详情缺乏',
  keywords: '专业术语',
  keywordsMessage:
    '您以下的{exp}经历详情描述没有用任何行业术语。请参考”写作助手”里面的写作建议进行修改！',
  expression: '表达专业度',
  exp: '经历',
  notQuantify: '成就表述未被量化',
  quantifyMessage:
    '您以下的{exp}经历详情描述没有用任何量化关键词。请参考”写作助手”里面的写作建议进行修改！',
  hasProblem: '有问题',
  sort: '排序',
  workTitle: '工作/实习经历',
  projTitle: '项目经历',
  volTitle: '志愿者经历',
  infoComplete: '信息完整度',
  layout: '简历版式',
  expValue: '经历含金量',
  present: '至今',
  valid_through: '有效期至: ',
  forever: '永久有效',
  delete: '删除',
  basicInfoNotCompleted: '您还未填写基本信息',
  EduInfoNotCompleted: '您还未填写教育背景',
  expReplacement: {
    workXp: '工作实习',
    projectXp: '项目',
    otherXp: '志愿者',
  },
  quantifyMetMessage:
    '您以下经历描述，对经历过程、结果，进行了量化表达，可以可以！！介绍过往经历，量化过程以及成就，展示了结果导向的表述思路，是现实工作场景中非常倡导的，您还可以参考“经历撰写助手”，进一步完善经历描述：',
  quantifyMetHeader: '经历详情已量化表达！',
  keywordsMetHeader: '经历详情已使用专业术语！',
  keywordsMetMessage:
    '您以下经历描述，使用到了专业术语，非常棒！！HR或者ATS，会通过捕捉专业术语，来判断您的背景资质，与岗位要求，是否吻合，您还可以参考“经历撰写助手”，进一步提炼经历描述：',
};

describe('test resume utils', () => {
  it('should reconstruct the resume object', () => {
    expect(flatten(null)).toEqual({});
    expect(flatten(123)).toEqual({});
    expect(flatten('')).toEqual({});
    expect(flatten()).toEqual({});
    expect(flatten([])).toEqual({});
    expect(flatten(testObj)).toEqual(flattenTestObj);
    expect(testObj.b.c).toEqual('nothing is true');
  });

  it('should flatten the resume object', () => {
    expect(reconstruct(flattenTestObj)).toEqual(testObj);
    expect(flattenTestObj['b.d.e']).toEqual('everything is permitted');
  });
});

describe('generateBasicFormRating', () => {
  it('should return the expected object when "complete" is true', () => {
    expect(
      generateBasicFormRating(
        {
          avatar: '321',
          linkedin: '123',
          weblink: '123',
          messages,
        },
        true,
      ),
    ).toEqual([]);
  });
  it('should return the expected object when "complete" is true', () => {
    expect(
      generateBasicFormRating(
        {
          avatar: '',
          linkedin: '',
          weblink: '',
          messages,
        },
        true,
      ),
    ).toEqual([
      {
        message: '填写领英账号会显得更专业哟！',
        name: '领英账号',
        noNav: false,
        section: 'basicInfo',
        selector: '#basic-linkedin',
        type: 'info-complete',
      },
      {
        message: '填写个人网站会显得更专业哟！',
        name: '个人网站',
        noNav: false,
        section: 'basicInfo',
        selector: '#basic-weblink',
        type: 'info-complete',
      },
      {
        message: '如果您投递中国地区的岗位，建议您添加个人简历照片',
        name: '上传照片',
        noNav: false,
        section: 'basicInfo',
        selector: '#upload-photo',
        type: 'layout',
      },
    ]);
  });
  it('should return the expected object when "completed" is false', () => {
    expect(
      generateBasicFormRating(
        {
          avatar: '',
          linkedin: '',
          weblink: '',
          messages,
        },
        false,
      ),
    ).toEqual([
      {
        message: '您还未填写基本信息',
        name: '基本信息',
        noNav: true,
        section: 'basicInfo',
        selector: '#basic-linkedin',
        type: 'info-complete',
      },
    ]);
  });
});

describe('generateLayoutRating', () => {
  it('should return the expected result []', () => {
    expect(generateLayoutRating(result, messages)).toEqual([]);
  });
  it('should return the expected result nonempty array', () => {
    const result = [{content: '■ asd', page: 2, type: 'list', y: 44}];
    expect(generateLayoutRating(result, messages)).toEqual([
      {
        message:
          '您的简历，尚未进行整页排版，请使用<b>“一键整页”</b>让我们帮您自动优化排版！',
        name: '页数不工整',
        noNav: true,
        prefix: '简历',
        section: 'basicInfo',
        selector: undefined,
        type: 'layout',
      },
    ]);
  });
});

describe('exp generator', () => {
  it('should return correct rating messages', () => {
    const exp = [
      {
        organization: '志愿者',
        role: '志愿者',
        rating: 'MEDIOCRE',
        bullets: [
          {
            bullet: '12312',
            keywords: [],
            numericWords: ['12312'],
          },
          {
            bullet: '123 asd 123, 99%',
            keywords: [],
            numericWords: ['123', '99%'],
          },
          {
            bullet: '123',
            keywords: [],
            numericWords: ['123'],
          },
          {
            bullet: '123',
            keywords: [],
            numericWords: ['123'],
          },
        ],
      },
      {
        organization: '123',
        role: '志愿者',
        rating: 'MEDIOCRE',
        bullets: [
          {
            bullet: '123.',
            keywords: [],
            numericWords: ['123'],
          },
          {
            bullet: 'dasd',
            keywords: [],
            numericWords: [],
          },
          {
            bullet: 'asd',
            keywords: [],
            numericWords: [],
          },
          {
            bullet: 'asd',
            keywords: [],
            numericWords: [],
          },
        ],
      },
    ];
    const section = 'otherXp';
    const eventType = 'volunteer';
    const sorted = false;

    expect(
      generateSuggestions(exp, section, eventType, sorted, messages),
    ).toEqual({
      companyArr: [],
      expArr: [
        {
          green: true,
          message:
            '志愿者经历，可以更加全面展示，专业技能之外的特质、能力，比如人际交往、活动策划等等，您目前只有2段志愿者经历，可以参考“经历写作助手”再多补充一些',
          name: '志愿者经历',
          type: 'exp-value',
        },
      ],
      keywordsArr: [
        {
          company: '志愿者',
          content: ['12312', '123 asd 123, 99%', '123', '123'],
          eventType: 'volunteer',
          index: 0,
          keywords: [[], [], [], []],
          name: '',
          position: '志愿者',
          section: 'otherXp',
          type: 'expression',
        },
        {
          company: '123',
          content: ['123.', 'dasd', 'asd', 'asd'],
          eventType: 'volunteer',
          index: 1,
          keywords: [[], [], [], []],
          name: '',
          position: '志愿者',
          section: 'otherXp',
          type: 'expression',
        },
      ],
      quantifyArr: [
        {
          company: '志愿者',
          content: ['12312', '123 asd 123, 99%', '123', '123'],
          eventType: 'volunteer',
          feedback: ['12312', '123', '99%', '123', '123'],
          index: 0,
          name: '',
          position: '志愿者',
          quantify: [['12312'], ['123', '99%'], ['123'], ['123']],
          section: 'otherXp',
          type: 'expression',
        },
        {
          company: '123',
          content: ['123.', 'dasd', 'asd', 'asd'],
          eventType: 'volunteer',
          index: 1,
          name: '',
          position: '志愿者',
          quantify: [['123'], [], [], []],
          section: 'otherXp',
          type: 'expression',
        },
      ],
      sortedArr: [
        {
          message: '您的经历模块，还未按时间排序，“一键排序”帮您自动排序',
          name: '时间排序',
          section: 'otherXp',
          type: 'layout',
        },
      ],
    });

    expect(generateSuggestions([], section, eventType, true, messages)).toEqual(
      {
        companyArr: [],
        expArr: [
          {
            green: false,
            message:
              '志愿者经历，可以更加全面展示，专业技能之外的特质、能力，比如人际交往、活动策划等等，您目前没有志愿者经历，可以参考“经历写作助手”再多补充一些',
            name: '志愿者经历',
            type: 'exp-value',
          },
        ],
        keywordsArr: [],
        quantifyArr: [],
        sortedArr: [],
      },
    );
  });
});

describe('educations', () => {
  const educations = [
    {
      id: 1463,
      schoolName: '悉尼大学',
      gpa: '1',
      startDate: '2020-11-01',
      graduateDate: '2020-12-01',
      major: '马克思主义哲学',
      degree: '本科',
      city: '川崎市',
      country: '日本',
    },
  ];

  const schools = [
    {
      institution: '悉尼大学',
      major: '马克思主义哲学',
      gpa: '1',
      rating: 'GREAT',
    },
  ];

  it('return expected res if not completed or shcools are empty', () => {
    expect(generateEducationRatings(educations, [], messages, false)).toEqual([
      {
        message: '您还未填写教育背景',
        name: '教育背景',
        noNav: true,
        section: 'education',
        selector: '',
        type: 'info-complete',
      },
    ]);
  });
  it('return expected res if not completed or shcools are empty', () => {
    expect(
      generateEducationRatings(educations, schools, messages, true),
    ).toEqual([
      [
        {
          message: '填写所获最高奖项显得更专业哟！',
          name: '所获最高奖项',
          section: 'education',
          selector: '.edu-0 #education-highest-award',
          type: 'info-complete',
        },
        {
          message: '填写其他获得奖项显得更专业哟！',
          name: '其他获得奖项',
          section: 'education',
          selector: '.edu-0 #education-other-award',
          type: 'info-complete',
        },
        {
          green: false,
          message: '您的GPA，在3.0以下，不建议您添加到简历上',
          name: '毕业GPA',
          section: 'education',
          selector: '.edu-0 #education-gpa',
          type: 'exp-value',
        },
        {
          green: true,
          message: '您的毕业院校悉尼大学，是世界名校，非常棒哟！',
          name: '学校知名度挺好',
          school: true,
          type: 'exp-value',
        },
      ],
    ]);
  });
});

describe('generateCertificeRating', () => {
  it('should return correct res', () => {
    expect(generateCertificeRating(0, messages)).toEqual([
      {
        message:
          '技能证书，是非常重要的资质证明，您这块背景较为薄弱，建议您查看快课，快速掌握专业技能，提升背景竞争力',
        name: '技能证书',
        section: 'certifications',
        type: 'exp-value',
      },
    ]);
  });
});
