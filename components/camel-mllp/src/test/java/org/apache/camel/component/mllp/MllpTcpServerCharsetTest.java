begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.mllp
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|mllp
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|CamelContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|EndpointInject
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|LoggingLevel
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
operator|.
name|RouteBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|mock
operator|.
name|MockEndpoint
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
operator|.
name|DefaultCamelContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|test
operator|.
name|AvailablePortFinder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|test
operator|.
name|junit
operator|.
name|rule
operator|.
name|mllp
operator|.
name|MllpClientResource
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|test
operator|.
name|junit4
operator|.
name|CamelTestSupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Rule
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|MllpTcpServerCharsetTest
specifier|public
class|class
name|MllpTcpServerCharsetTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|TEST_MESSAGE
specifier|static
specifier|final
name|String
name|TEST_MESSAGE
init|=
literal|"MSH|^~\\&|KinetDx|UCLA Health System|||201801301506||ORU^R01|18030543772221|P|2.3^^||||||ISO_IR 100|"
operator|+
literal|'\r'
operator|+
literal|"PID|1||1117922||TESTER^MARY||19850627|F"
operator|+
literal|'\r'
operator|+
literal|"OBR|1||55510818|ECH10^TRANSTHORACIC ECHO ADULT COMPLETE^IMGPROC|||20180126103542|||||||||029137^LEIBZON^ROMAN^^^^^^EPIC^^^^PROVID|||||Y|20180130150612|||F"
operator|+
literal|"|||||||029137^Leibzon^Roman^^MD^^^^EPIC^^^^PROVID"
operator|+
literal|'\r'
operator|+
literal|"DG1|1|I10|^I10^ HTN (essential)^I10"
operator|+
literal|'\r'
operator|+
literal|"DG1|2|I10|R94.31^Abnormal EKG^I10"
operator|+
literal|'\r'
operator|+
literal|"OBX|1|FT|&GDT||  Thousand Oaks Cardiology||||||F"
operator|+
literal|'\r'
operator|+
literal|"OBX|2|FT|&GDT|| 100 Moody Court, Suite 200||||||F"
operator|+
literal|'\r'
operator|+
literal|"OBX|3|FT|&GDT||  Thousand Oaks, CA 91360||||||F"
operator|+
literal|'\r'
operator|+
literal|"OBX|4|FT|&GDT||    Phone: 805-418-3500||||||F"
operator|+
literal|'\r'
operator|+
literal|"OBX|5|FT|&GDT|| ||||||F"
operator|+
literal|'\r'
operator|+
literal|"OBX|6|FT|&GDT||TRANSTHORACIC ECHOCARDIOGRAM REPORT||||||F"
operator|+
literal|'\r'
operator|+
literal|"OBX|7|FT|&GDT|| ||||||F"
operator|+
literal|'\r'
operator|+
literal|"OBX|8|FT|&GDT||Patient Name:              Date of Exam:   1/26/2018||||||F"
operator|+
literal|'\r'
operator|+
literal|"OBX|9|FT|&GDT||Medical Rec #:                    Accession #:         ||||||F"
operator|+
literal|'\r'
operator|+
literal|"OBX|10|FT|&GDT||Date of Birth:                   Height:         74 in||||||F"
operator|+
literal|'\r'
operator|+
literal|"OBX|11|FT|&GDT||Age:                             Weight:         230 lbs||||||F"
operator|+
literal|'\r'
operator|+
literal|"OBX|12|FT|&GDT||Gender:                          BSA:            2.31 mÂ²||||||F"
operator|+
literal|'\r'
operator|+
literal|"OBX|13|FT|&GDT||Referring Physician: 029137 ROMAN LEIBZON Blood Pressure: /||||||F"
operator|+
literal|'\r'
operator|+
literal|"OBX|14|FT|&GDT||Diagnosis: I10- HTN (essential); R94.31-Abnormal EKG||||||F"
operator|+
literal|'\r'
operator|+
literal|"OBX|15|FT|&GDT|| ||||||F"
operator|+
literal|'\r'
operator|+
literal|"OBX|16|FT|&GDT||MEASUREMENTS:||||||F"
operator|+
literal|'\r'
operator|+
literal|"OBX|17|FT|&GDT||LVIDd (2D)     5.16 cm LVIDs (2D)   3.14 cm||||||F"
operator|+
literal|'\r'
operator|+
literal|"OBX|18|FT|&GDT||IVSd (2D)      0.93 cm LVPWd (2D)   1.10 cm||||||F"
operator|+
literal|'\r'
operator|+
literal|"OBX|19|FT|&GDT||LA (2D)        4.00 cm Ao Root (2D) 3.00 cm||||||F"
operator|+
literal|'\r'
operator|+
literal|"OBX|20|FT|&GDT||FINDINGS:||||||F"
operator|+
literal|'\r'
operator|+
literal|"OBX|21|FT|&GDT||Left Ventricle: The left ventricular size is normal. Left ventricular wall thickness is normal. LV wall motion is normal. The ejection fraction by Simpson's "
operator|+
literal|"Biplane method is 60 %. Normal LV diastolic function. MV deceleration time is 127 msec.||||||F"
operator|+
literal|'\r'
operator|+
literal|"OBX|22|FT|&GDT||MV E velocity is 0.77 m/s. MV A velocity is 0.56 m/s. E/A ratio is 1.36.||||||F"
operator|+
literal|'\r'
operator|+
literal|"OBX|23|FT|&GDT||Lateral E/e' ratio is 6.0. Medial E/e' ratio is 8.7.||||||F"
operator|+
literal|'\r'
operator|+
literal|"OBX|24|FT|&GDT||Left Atrium: The left atrium is mildly dilated in size. The LA Volume index is 30.8 ml/mÂ².||||||F"
operator|+
literal|'\r'
operator|+
literal|"OBX|25|FT|&GDT||Right Atrium: The right atrium is normal in size. RA area is 17 cm2. RA volume is 42 ml.||||||F"
operator|+
literal|'\r'
operator|+
literal|"OBX|26|FT|&GDT||Right Ventricle: The right ventricular size is normal. Global RV systolic function is normal. TAPSE 24 mm. The RV free wall tissue Doppler S' wave measures 16.7 cm/s. "
operator|+
literal|"The right ventricle basal diameter measures 26 mm. The right ventricle mid cavity measures 23 mm. The right ventricle longitudinal diameter measures 65 mm.||||||F"
operator|+
literal|'\r'
operator|+
literal|"OBX|27|FT|&GDT||Mitral Valve: Mitral annular calcification noted. Trace mitral valve regurgitation. There is no mitral stenosis.||||||F"
operator|+
literal|'\r'
operator|+
literal|"OBX|28|FT|&GDT||Aortic Valve: The aortic valve appears trileaflet. Trace aortic valve regurgitation. The LVOT velocity is 1.16 m/s. The peak aortic valve velocity is 1.19 m/s. "
operator|+
literal|"No aortic valve stenosis.||||||F"
operator|+
literal|'\r'
operator|+
literal|"OBX|29|FT|&GDT||Tricuspid Valve: The tricuspid valve appears normal in structure. Trace tricuspid regurgitation is present. The peak velocity of TR is 2.55 m/s.||||||F"
operator|+
literal|'\r'
operator|+
literal|"OBX|30|FT|&GDT||Pulmonic Valve: Trivial pulmonary valve regurgitation. No evidence of pulmonary valve stenosis.||||||F"
operator|+
literal|'\r'
operator|+
literal|"OBX|31|FT|&GDT||Pericardium: There is no pericardial effusion.||||||F"
operator|+
literal|'\r'
operator|+
literal|"OBX|32|FT|&GDT||Aorta: The aortic root size is normal. The aortic valve annulus measures 25 mm. The sinus of Valsalva measures 33 mm. The sinotubular junction measures 30 mm. "
operator|+
literal|"The proximal ascending aorta measures 30 mm.||||||F"
operator|+
literal|'\r'
operator|+
literal|"OBX|33|FT|&GDT||Pulmonary Artery: Based on the acceleration time in the RV outflow tract, the PA pressure is not likely to be elevated. The calculated pulmonary artery pressure "
operator|+
literal|"(or right ventricular systolic pressure) is 29 mmHg, if the right atrial pressure is 3 mmHg. Normal PA systolic pressure.||||||F"
operator|+
literal|'\r'
operator|+
literal|"OBX|34|FT|&GDT||IVC: Normal inferior vena cava in diameter with respiratory variation consistent with normal right atrial pressure.||||||F"
operator|+
literal|'\r'
operator|+
literal|"OBX|35|FT|&GDT|| ||||||F"
operator|+
literal|'\r'
operator|+
literal|"OBX|36|FT|&GDT||IMPRESSION:||||||F"
operator|+
literal|'\r'
operator|+
literal|"OBX|37|FT|&GDT|| 1. Normal left ventricular size.||||||F"
operator|+
literal|'\r'
operator|+
literal|"OBX|38|FT|&GDT|| 2. The calculated ejection fraction (Simpson's) is 60 %.||||||F"
operator|+
literal|'\r'
operator|+
literal|"OBX|39|FT|&GDT|| 3. Normal LV diastolic function.||||||F"
operator|+
literal|'\r'
operator|+
literal|"OBX|40|FT|&GDT|| 4. Mildly dilated left atrium in size.||||||F"
operator|+
literal|'\r'
operator|+
literal|"OBX|41|FT|&GDT||029137 Roman Leibzon MD||||||F"
operator|+
literal|'\r'
operator|+
literal|"OBX|42|FT|&GDT||Electronically signed by 029137 Roman Leibzon MD on 1/30/2018 at 3:06:12 PM||||||F"
operator|+
literal|'\r'
operator|+
literal|"OBX|43|FT|&GDT|| ||||||F"
operator|+
literal|'\r'
operator|+
literal|"OBX|44|FT|&GDT||Sonographer: Liana Yenokyan||||||F"
operator|+
literal|'\r'
operator|+
literal|"OBX|45|FT|&GDT|| ||||||F"
operator|+
literal|'\r'
operator|+
literal|"OBX|46|FT|&GDT||*** Final ***||||||F\r"
decl_stmt|;
DECL|field|TARGET_URI
specifier|static
specifier|final
name|String
name|TARGET_URI
init|=
literal|"mock://target"
decl_stmt|;
annotation|@
name|Rule
DECL|field|mllpClient
specifier|public
name|MllpClientResource
name|mllpClient
init|=
operator|new
name|MllpClientResource
argument_list|()
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
name|TARGET_URI
argument_list|)
DECL|field|target
name|MockEndpoint
name|target
decl_stmt|;
annotation|@
name|Override
DECL|method|createCamelContext ()
specifier|protected
name|CamelContext
name|createCamelContext
parameter_list|()
throws|throws
name|Exception
block|{
name|mllpClient
operator|.
name|setMllpHost
argument_list|(
literal|"localhost"
argument_list|)
expr_stmt|;
name|mllpClient
operator|.
name|setMllpPort
argument_list|(
name|AvailablePortFinder
operator|.
name|getNextAvailable
argument_list|()
argument_list|)
expr_stmt|;
name|DefaultCamelContext
name|context
init|=
operator|(
name|DefaultCamelContext
operator|)
name|super
operator|.
name|createCamelContext
argument_list|()
decl_stmt|;
name|context
operator|.
name|setUseMDCLogging
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|context
operator|.
name|setName
argument_list|(
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getSimpleName
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|context
return|;
block|}
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
name|String
name|routeId
init|=
literal|"mllp-sender"
decl_stmt|;
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|fromF
argument_list|(
literal|"mllp://%d?receiveTimeout=1000&readTimeout=500&charsetName=ISO-IR-100"
argument_list|,
name|mllpClient
operator|.
name|getMllpPort
argument_list|()
argument_list|)
operator|.
name|log
argument_list|(
name|LoggingLevel
operator|.
name|INFO
argument_list|,
name|routeId
argument_list|,
literal|"Sending Message"
argument_list|)
operator|.
name|to
argument_list|(
name|target
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
annotation|@
name|Test
DECL|method|testReceiveMessageWithInvalidMsh18 ()
specifier|public
name|void
name|testReceiveMessageWithInvalidMsh18
parameter_list|()
throws|throws
name|Exception
block|{
name|target
operator|.
name|expectedMinimumMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mllpClient
operator|.
name|connect
argument_list|()
expr_stmt|;
name|mllpClient
operator|.
name|sendMessageAndWaitForAcknowledgement
argument_list|(
name|TEST_MESSAGE
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|(
literal|5
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testReceiveMessageWithValidMsh18 ()
specifier|public
name|void
name|testReceiveMessageWithValidMsh18
parameter_list|()
throws|throws
name|Exception
block|{
name|target
operator|.
name|expectedMinimumMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mllpClient
operator|.
name|connect
argument_list|()
expr_stmt|;
name|mllpClient
operator|.
name|sendMessageAndWaitForAcknowledgement
argument_list|(
name|TEST_MESSAGE
operator|.
name|replace
argument_list|(
literal|"ISO_IR 100"
argument_list|,
literal|"ISO-IR-100"
argument_list|)
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|(
literal|5
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

