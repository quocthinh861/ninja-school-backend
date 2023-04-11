 package com.Server.ninja.Server;

public class Talk {
    private static final String[][] TEXTVIE;
    private static final String[] TEXTVIETASK;

    private static final String[] TEXTFINISH;

    private static final String[] TEXTGETTASk;

    public static String get(final int type, final int num) {
        try {
            if (type == 0) {
                return Talk.TEXTVIE[num][Util.nextInt(Talk.TEXTVIE[num].length)];
            }
        } catch (Exception ex) {
        }
        return String.format("ERROR[%d:%d]", type, num);
    }

    public static String getTask(final int type, final int num) {
        try {
            if (type == 0) {
                return Talk.TEXTVIETASK[num];
            }
            if (type == 1) {
                return Talk.TEXTFINISH[num];
            }
            if (type == 2) {
                return Talk.TEXTGETTASk[num];
            }
        } catch (Exception ex) {
        }
        return String.format("ERROR[%d:%d]", type, num);
    }

    static {
        TEXTVIE = new String[][]{new String[0], new String[0], new String[0], {"\u0110i \u0111\u01b0\u1eddng c\u1ea7n mang \u00edt d\u01b0\u1ee3c ph\u1ea9m nh\u00e9", "Kh\u00f4ng mang theo HP, Hp b\u00ean m\u00ecnh, con s\u1ebd g\u1eb7p nguy hi\u1ec3m."}, {"Th\u1ee9c \u0103n c\u1ee7a ta l\u00e0 ngon nh\u1ea5t r\u1ed3i!", "Hahaha, ch\u1eafc ng\u01b0\u01a1i \u0111i \u0111\u01b0\u1eddng c\u0169ng m\u1ec7t r\u1ed3i."}, {"H\u00e3y an t\u00e2m gia \u0111\u1ed3 cho ta n\u00e0o!", "Tr\u00ean ng\u01b0\u1eddi c\u1ee7a ng\u01b0\u01a1i to\u00e0n l\u00e0 nh\u1eefng \u0111\u1ed3 c\u00f3 gi\u00e1 tr\u1ecb, Sao kh\u00f4ng c\u1ea5t b\u1edbt \u1edf \u0111\u00e2y?", "Ta gi\u1eef \u0111\u1ed3 ch\u01b0a h\u1ec1 \u0111\u1ec3 th\u1ea5t l\u1ea1c bao gi\u1edd."}, {"N\u00e2ng c\u1ea5p trang b\u1ecb: Uy t\u00ednh, gi\u00e1 c\u1ea3 ph\u1ea3i ch\u0103ng"}, {"Ng\u1ef1a c\u1ee7a ta r\u1ea5t kh\u1ecfe, c\u00f3 th\u1ec3 ch\u1ea1y ng\u00e0n d\u1eb7m"}};
        TEXTVIETASK = new String[]{
                "Việc đầu tiên con cần là đi nói chuyện với những người trong làng. Con đồng ý chứ?",//0
                "Chào con, ta là Kiriko, chuyên bán dược phẩm chữa trị vết thương",//1
                "Ta là Tabemono. Cửa hàng của ta bán rất nhiều thức ăn, giúp tăng cường sức khỏe",//2
                "Muốn nâng cấp trang bị? Hãy tìm Kenshito này.",//3
                "Chào con, ta đứng đây giúp đỡ mọi người trao đổi, lưu thông tiền với nhau.",//4
                "Ta là Kamakura, ta sẽ cất giữ đồ đạc cho ngươi.",//5
                "Ngươi muốn đến làng nào, hãy đến gặp ta. Ta sẽ chở đi",//6
                "Con nhớ ông Tabemono bán thức ăn chứ? Hãy đến gặp, ông ấy có vài câu hỏi kiểm tra con đấy.",//7
                "Nhanh lên nhé, hãy chọn Menu/Nhiệm vụ để biết mình cần nói chuyện với những ai.",//8
                "Haha, con đã gặp những người đó rồi chứ? Con cứ đi tìm hiểu quanh làng, khi nào thấy quen thuộc hãy đến gặp ta lần nữa.",//9
                "Ông Tabemono con đã từng nói chuyện 1 lần rồi, ông ta đứng đằng kia kìa.",//10
                "Con có muốn nhận làm nhiệm vụ này không",//11
                "Trả lời nhanh cho ta: ông Kiriko làm gì",//12
                "Con biết ta đứng đây làm gì không",//13
                "Công việc của Kamakura là gì",//14
                "Thế còn Kenshinto, ông ấy làm gì?",//15
                "Umayaki đứng trong làng làm gì?",//16
                "Giữ rương đồ",//17
                "Bán thuốc HP,MP",//18
                "Bán thức ăn",//19
                "Kéo xe qua các làng",//20
                "Người kéo xe",//21
                "Người bán thức ăn",//22
                "Nâng cấp trang bị",//23
                "Trả lời sai. Suy nghĩ thật kĩ rồi chọn lại. %s",//24
                "Haha, tốt, hãy quay về tìm trưởng làng, ta đã gửi ông ấy 1 món quà đặc biệt cho con.",//25
                "Con đã gặp ông Tabemono? Tốt lắm, đây là một thanh kiếm gỗ mà ông ấy tặng con. Kiểm tra trong Menu?Hành trang nhé!",//26
                "Rất tốt, Con đã có kiếm. Ta muốn con học cách sử dụng nó bằng cách đánh ngã 10 con bù nhìn rơm đằng kia.",//27
                "Con chú ý cần mở: Menu/Hành trang để trang bị vũ khí cho mình nhé.",//28
                "Đó chỉ là bù nhìn thôi. Không có gì phải tự hào. Cứ luyện tập đi, khi nào thấy có khả năng thì quay lại gặp ta.",//29
                "Tập đủ chưa vậy? Nếu đủ thì con hãy mua ít thức ăn, ra đánh bọn ốc sên và cóc xanh quấy phá mùa màng ngoài làng đi.",//30
                "Ta nhắc lại lần nữa: Hãy chọn Menu/Nhiệm vụ để biết cách làm như thế nào!",//31
                "Trưởng làng nhờ ta nói với con: Hãy sử dụng thức ăn trước khi ra khỏi làng.",//32
                "Tốt lắm, bay giờ con có thể ra khỏi làng làm nhiệm vụ rồi.",//33
                "Con làm tốt lắm, nhưng bọn cóc ấy vẫn còn quá nhiều. Lần sau gặp ta, ta sẽ hướng dẫn con tạo thêm trang bị cho mình.",//34
                "Con thu thập 1 ít lông nhím và da thỏ, ta sẽ may cho con 1 cái quần thật đẹp.",//35
                "Lưu ý là ta chỉ cho phép con làm việc này khi con đạt cấp 5, vì vậy hãy luyện tập trước đi",//36
                "Đủ vật liệu rồi. Tốt tốt, kiể, tra trong hành trang của con nhé! Đừng ngạc nhiên vì sao ta có thể may quá nhanh.",//37
                "Con đạt cấp 6 chưa? Nếu đã đạt thì mang về cho ta 15 bông thảo dược từ thác Kitajima về giúp ta nhé.",//38
                "Thác Kitajima khá xa đấy, hãy chọn Menu/Bản đồ để biết cách đi đến đó nhé.",//39
                "Ta cho con một ít bình HP và MP được làm từ thảo dược con mang về. Nếu muốn mua thêm, con có thể trở lại đây gặp ta",//40
                "Làng ta nằm giữa 3 ngôi trường lớn. Khi con đạt cấp 7, hãy đến khu vực quanh trường để tìm hiểu xem.",//41
                "Hãy mở Menu/bản đồ để biết đường đi. Tìm hiểu xem mình thích hợp ngôi trường nào nhất con nhé!",//42
                "Thế nào? Con đã biết các ngôi trường đó dậy gì chưa? Nếu biết thì nhanh đến gặp trưởng làng nhé!",//43
                "Sắp tới, con sẽ phải chọn một ngôi trường để theo học. Hãy gặp Tabemono để biết kiến thức cơ bản trước khi vào trường.",//44
                "Chúc con may mắn nhé!",//45
                "Tiền yên và vật phẩm khóa",//46
                "Tiền xu và vật không khóa phẩm khóa",//47
                "Tiền yên, xu, và vật phẩm không khóa",//48
                "Con thử trả lời nhé! Con có thể buôn bán trao đổi những loại vật phẩm nào?",//49
                "Có bao nhiêu trường học quanh đây?",//50
                "3 trường",//51
                "2 trường",//52
                "4 trường",//53
                "Có tổng cộng bao nhiêu lớp học?",//54
                "3 lớp",//55
                "6 lớp",//56
                "12 lớp",//57
                "Nội công bao gồm những lớp nào?",//58
                "Kiếm, phi tiêu, quạt",//59
                "Phi tiêu, quạt, cung",//60
                "Kunai, cung, đao",//61
                "Ngoại công bao gồm những lớp nào?",//62
                "Phi tiêu, đao, cung",//63
                "Quạt, kiếm, kunai",//64
                "Kiếm, kunai, đao",//65
                "Tốt, hãy tìm Kamakura để biết những kiến thức khác",//66
                "Chào. Cho ta biết con đang có bao nhiêu ô trống trong hành trang?",//67
                "10 ô",//68
                "20 ô",//69
                "30 ô",//70
                "Lưu tạo độ mặc định nhằm mục đích gì?",//71
                "Là nơi về khi bị thương",//72
                "Nơi xuất hiện khi đăng nhập",//73
                "Nơi đứng của người giữ rương",//74
                "Có mấy loại tiền tệ ở thế giới Ninja này?",//75
                "1 loại",//76
                "2 loại",//77
                "3 loại",//78
                "Tiền yên trong kiếm được bằng cách nào?",//79
                "Làm nhiệm vụ, tham gia hoạt động",//80
                "Nhặt được khi đánh quái",//81
                "Cả 2 trường hợp",//82
                "Loại tiền nào có thể chuyển đổi qua lại giữa các người chơi?",//83
                "Tiền yên",//84
                "Tiền xu",//85
                "Tiền lượng",//86
                "Rất tốt, tiếp theo hãy tìm Kenshito, ông ấy có một số câu hỏi cho con đấy!",//87
                "Con có biết khi nâng cấp trang bị thì cần những gì không?",//88
                "Đá + yên hoặc xu",//89
                "Đá",//90
                "Yên hoặc xu",//91
                "Tách trang bị sau khi nâng cấp sẽ nhận được gì?",//92
                "Xu + 100% Đá đã ép vào",//93
                "50% Đá đã ép vào",//94
                "Không được gì cả",//95
                "Trang bị được nâng cấp tối đa đến cấp mấy?",//96
                "Cấp 12",//97
                "Cấp 14",//98
                "Cấp 16",//99
                "Trang bị căn bản có mấy loại?",//100
                "1 loại",//101
                "2 loại",//102
                "3 loại",//103
                "Sau khi nâng cấp trang bị sẽ như thế nào?",//104
                "Không thay đổi",//105
                "Lên cấp và chỉ số cao hơn",//106
                "Trông đẹp hơn",//107
                "Con cũng đã biết khá nhiều rồi đấy. Hãy quay về gặp trưởng làng, ông ấy đang chờ con đó",//108
                "Chúc mừng con, ta đang liên hệ với các thầy hiệu trưởng. Khi đạt cấp 9, hãy đến gặp ta để nhận giấy giới thiệu.",//109
                "Con cứ đến gặp thầy, cô hiệu trưởng và bảo với họ do ta giới thiệu con đến đó",//110
                "Đi đường cẩn thận, sau khi nói chuyện xong với 3 thầy quay về đây gặp lại ta!",//111
                "Con cũng thấy rồi đó, ở trường của ta thuộc hệ hỏa, binh khí sử dụng là kiếm và phi tiêu!",//112
                "Tất cả mọi thứ đều đóng băng thì thật tuyệt, vũ khí ta thường dùng là kunai và cung!",//113
                "Nhanh và mạnh, đó là Phong, gió của ta... kết hợp với là đao và quạt, không gì có thể cản được!",//114
                "Tốt, bây giờ con đã có thể xin nhập học từ thầy, cô hiệu trưởng ở các trường. Nhưng trước khi ra khỏi làng..."
                        + "\nta muốn con cất giữ thật kỹ viên ngọc 4 sao này, đây là bảo vật gia truyền của làng ta..."
                        + "\nngoài nó ra vẫn còn thêm 6 viên nữa được cất giữ ở 6 ngôi làng bên cạnh, ta muốn con hãy thu thập cho đủ bộ..."
                        + "\nđể giải mã bí mật bên trong 7 viên ngọc. Các trưởng làng sẽ tặng nó cho con sau mỗi lần con giúp đỡ họ..."
                        + "\nCố gắng làm việc thật tốt con nhé. Hãy sử dụng khả di lệnh (Menu/Hành trang) ta tặng để đi cho nhanh!",//115
                "Con có thể tăng điểm tiềm năng và kỹ năng rồi. Hãy vào Menu/ Bản thân để tăng cho ta xem thử.",//116
                "Tốt, Con hãy nhớ vào Menu/Bản thân để tăng điểm cho mình mỗi khi lên cấp nhé!",//117
                "Bài học đầu tiên của con là đi đánh 50 rùa vàng, 20 nhện đốm và 10 quỷ 1 mắt.",//118
                "Chúng đều ở xung quanh, không xa 3 ngôi trường của chúng ta đâu",//119
                "Ngoài sự mong đợi của ta, con làm khá lắm, chúc mừng con!",//120
                "Rất nhiều bạn bè đang ở xunh quanh, con hãy đi mau đi làm quen với họ.",//121
                "Có thêm 1 người bạn còn hơn là thêm 1 kẻ thù con nhé.",//122
                "Đi đường gặp nhiều may mắn con nhé.", //123
                "Cố gắng phòng thủ thật tốt nhé", //124
                "Đi đường cẩn thận con nhé.",//125
                "Con hãy giúp ta chuyển những bức thư này đến Hashimoto, Fujiwara và Nao giùm ta nhé!", // 126
                "Để trở thành 1 ninja giỏi ngươi phải rèn luyện thật nhiều.", // 127
                "Jaian! khi nào thì con mới về...", // 128
                "Rừng thiêng nước độc, con hãy cẩn thận", // 129
                "Chỉ có nước trong Hang Ha mới có tinh chế ra được loại thuốc cần thiết, hãy cố gắng giúp ta nhé.", // 130
                "Thử thách lần này không đơn giản, con nhớ chuẩn bị thuốc hồi phục Hp trước khi đi!", // 131
                "Gần đây Quỷ Băng và Quỷ Hoa rất hay quấy rối dân làng ta, con hãy giúp ta trừng trị chúng", // 132
                "Quỷ xương khô rất hung dữ con hãy cẩn thận nhé.", // 133
                "Địa đồ ẩn chứa rất nhiều bí mật, con hãy nhanh chóng tìm nó về cho ta nhé.", // 134
                "Nhiệm vụ lần này đòi hỏi con phải quan sát thật tỉ mỉ để nhận ra huyết mạch đào bảo.", //135
                "Có công mài sắt có ngày nên kim, cố gắng lên con nhé", // 136
                "Tinh thể băng có rất nhiều công dụng tốt con giúp ta mang về đây thật nhiều tinh thể băng nhé.", //137
                "...", // 138
                "Trừ hại Ốc ma, Chuột hoang, bảo vệ mùa màng cho dân làng ta.", // 139
                "Áp lực càng lớn tiến bộ càng nhanh.", // 140
                "Nhiệm vụ hàng ngày và tà thú ẩn chứa rất nhiều phần thưởng", //141
                "Có thực mới vực được đạo.", // 142
                "Nhiệm vụ lần này không phải đơn giản, ta muốn con phải kiên trì, nhẫn nại và không được bỏ cuộc.", // 143
                "Con nhớ chuẩn bị đầy đủ thức ăn và thảo dược rồi hãy lên đường."
        };
        TEXTFINISH = new String[]{
                "Thế nào rồi, con đã có thêm nhiều bạn bè?",// 0
                "Tốt lắm, nhìn con đã có nhiều sự thay đổi", // 1
                "Khá lắm, các thầy cô đều đánh giá rất cao về khả năng của con!",//2
                "Cảm ơn con nhé, ta có món quà nhỏ dành cho con!", // 3
                "Làm tốt lắm, hãy nhận lấy phần thưởng của mình!", // 4
                "Khá lắm, hãy tiếp tục hành trình!", // 5
                "Ta không biết nói gì hơn, cảm ơn con nhiều lắm!", //6
                "Cảm ơn con, xin hãy nhận lấy tấm lòng của ta!", // 7
                "Thật vui mừng khi thấy con trở về, chỉ cần trễ thêm 1 ngày nữa thì ta cũng không biết làm thế nào để cứu Jaian...!", // 8
                "Ôi bảo vật của làng ta, cuối cùng thì nó cũng được quay về đúng vị trí. Hãy nhận lấy phần thưởng mà con đáng được nhận.", //9
                "Có lẽ chúng sẽ không dám quấy rối dân làng ta nữa. Cám ơn con! ta có phần thưởng cho con đây.", //10
                "Chìa khoá mở cửa cơ quan bí mật khám phá những bí ẩn mới, công lao của con quả không nhỏ hãy nhận lấy phần thưởng của mình nhé", // 11
                "Ta có phần thưởng cho con đây! Chà!!! Vẫn còn 1 bí mật đang ẩn giấu trong tấm địa đồ này.", // 12
                "Đây là phần thưởng của ta dành cho con", //13
                "Đây là phần thưởng cho sự chăm chỉ của con.", //14
                "Ta có phần quà nhỏ dành cho tấm lòng của con đã giúp đỡ ta.", //15
                "Thật tốt quá! Giờ ta có thể làm thuốc cho dân làng mình rồi. Con nhận lấy món quà này thay cho lời cảm ơn nhé.", //16
                "Đây là phần thưởng cho sự kiên trì của con.", //17
                "Đây là phần thưởng cho sự dũng cảm của con.", //18
                "Đây là phần thưởng cho sự chăm chỉ của con.", //19
                "Đây là phần thưởng cho lòng tốt của con.", //20
                "Ha ha đôi guốc gỗ của ta, cuối cùng thì ta cũng tìm lại được. Phần thưởng này xứng đáng cho tình kiên trì và sự nhẫn nại của con.", //21
        };
        TEXTGETTASk = new String[]{
                "Con sẽ có cơ hội nhặt được những món trang bị trên người được rớt ra từ quái vật.",// 0
                "Lần này ta muốn con gặp thầy cô hiệu trưởng các trường để xem trình độ võ công của họ như thế nào.", // 1
                "Con có thể giúp ta kiếm nguyên liệu chế tạo trang sức?",//2
                "Công việc đưa thư cần phải dũng cảm, nhanh nhẹn, không ngại gian khó!", // 3
                "Hmmmmmmmmmmmmmmmm", // 4
                "Cháu của già đi lạc trong rừng rồi, con hãy giúp già này đưa nó về!", // 5
                "Do bị kiệt sức nhiều ngày, Jaian sau khi trở về đã ngã bệnh. Ta muốn con thu thập 1 ít nguyên liệu cần thiết để chữa bệnh cho Jaian.", //6
                "Nguyên liệu không thì vẫn chưa đủ, con hãy giúp ta lấy thêm 1 ít nước từ hang đá và mang về đây.", // 7
                "Vật trấn ngư chi bảo của làng mà Jaian mang về đã bị quái vật đánh cắp trên đường, con hãy giúp ta tìm nó về đây.", // 8
                "Gần đây Quỷ Băng và Quỷ Hoa rất hay quấy rối dân làng ta, con hãy giúp ta trừng trị chúng.", //9
                "Những con quỷ xương khô ẩn trong hang tối mang trên mình chìa khoá mở ra cánh của bí mật. Con hãy giúp ta nhanh chóng thu thập chìa khoá trên người chúng nhé!", // 10
                "Ta cho con chiếc chìa khoá này, con hãy nhanh chóng đến Hang Meiro khám phá những bí ẩn nằm sâu trong đó và mang về cho ta tấm địa đồ quý báu nhé.", // 11
                "Ta đã xem qua tấm địa đồ nhưng vẫn không thể nào hiểu được tiềm ẩn bên trong, chi bằng con hãy giữ nó tìm hiểu xem bên trong là gì.", // 12
                "Con đường phía trước còn nhiều gian khổ, con hãy chăm chỉ luyện tập nhiều hơn nhé.", // 13
                "Quái vật rất hung dữ con hãy cẩn thận khi thu thập tinh thể băng.", // 14
                "Xác dơi lửa có thể chế biến thuốc chữa bệnh rất tốt, con hãy giúp ta tìm về đây thật nhiều xác dơi lửa nhé.", //15
                "Đã nhiều năm nay, ruộng đồng của làng ta luôn bị lũ Ốc ma và Chuột hoang quấy phá.", // 16
                "Một sự khởi đầu mới cho quá trình luyện tập của con, hi vọng con sẽ hoàn thành tốt nhiệm vụ lần này", // 17
                "Hãy siêng năng lên con sẽ có thêm nhiều cống hiến cho gia tộc qua nhiệm vụ lần này.", // 18
                "Làng ta phải tích trữ lương thực, đề phòng thiên tai bất ngờ, con hãy giúp dân làng lần này nhé.", // 19
                "Ta đã đánh rơi một vật phẩm ở phía dưới con suối, con hãy giúp ta tìm lại nó. Còn đây là cần câu, có lẽ con sẽ cần dùng đến nó.", // 20
                "Gần đây Mada thường quấy nhiễu dân làng Sanzu ta, Con hãy giúp dân làng tiêu diệt chúng.", //21
        };
    }
}
