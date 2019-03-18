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
name|org
operator|.
name|junit
operator|.
name|Before
import|;
end_import

begin_comment
comment|/**  * Tests for the MllpEndpoint class.  */
end_comment

begin_class
DECL|class|MllpEndpointTest
specifier|public
class|class
name|MllpEndpointTest
block|{
DECL|field|MSH_SEGMENT
specifier|static
specifier|final
name|String
name|MSH_SEGMENT
init|=
literal|"MSH|^~\\&|0|90100053675|JCAPS|CC|20131125122938|RISMD|ORM|28785|D|2.3"
decl_stmt|;
comment|// @formatter:off
DECL|field|REMAINING_SEGMENTS
specifier|static
specifier|final
name|String
name|REMAINING_SEGMENTS
init|=
literal|"PID|1||4507626^^^MRN^MRN||RAD VALIDATE^ROBERT||19650916|M||U|1818 UNIVERSITY AVE^^MADISON^WI^53703^USA^^^||(608)251-9999|||M|||579-85-3510||| "
operator|+
literal|'\r'
operator|+
literal|"PV1||OUTPATIENT|NMPCT^^^WWNMD^^^^^^^DEPID||||011463^ZARAGOZA^EDWARD^J.^^^^^EPIC^^^^PROVID|011463^ZARAGOZA^EDWARD^J.^^^^^EPIC^^^^PROVID"
operator|+
literal|"|||||||||||90100053686|SELF||||||||||||||||||||||||201311251218|||||||V"
operator|+
literal|'\r'
operator|+
literal|"ORC|RE|9007395^EPC|9007395^EPC||Final||^^^201311251221^201311251222^R||201311251229|RISMD^RADIOLOGY^RADIOLOGIST^^|||SMO PET^^^7044^^^^^SMO PET CT||||||||||||||||I"
operator|+
literal|'\r'
operator|+
literal|"OBR|1|9007395^EPC|9007395^EPC|IMG7118^PET CT LIMITED CHEST W CONTRAST^IMGPROC^^PET CT CHEST||20131125|||||Ancillary Pe|||||||NMPCT|MP2 NM INJ01^MP2 NM INJECTION ROOM 01^PROVID"
operator|+
literal|"|||201311251229||NM|Final||^^^201311251221^201311251222^R||||^test|E200003^RADIOLOGY^RESIDENT^^^^^^EPIC^^^^PROVID"
operator|+
literal|"|812644^RADIOLOGY^GENERIC^ATTENDING 1^^^^^EPIC^^^^PROVID~000043^RADIOLOGY^RADIOLOGISTTWO^^^^^^EPIC^^^^PROVID|U0058489^SWAIN^CYNTHIA^LEE^||201311251245"
operator|+
literal|'\r'
operator|+
literal|"OBX|1|ST|&GDT|1|[11/25/2013 12:28:14 PM - PHYS, FIFTYFOUR]50||||||Final||||"
operator|+
literal|'\r'
operator|+
literal|'\n'
decl_stmt|;
comment|// @formatter:on
DECL|field|TEST_MESSAGE
specifier|static
specifier|final
name|String
name|TEST_MESSAGE
init|=
name|MSH_SEGMENT
operator|+
literal|'\r'
operator|+
name|REMAINING_SEGMENTS
decl_stmt|;
DECL|field|instance
name|MllpEndpoint
name|instance
decl_stmt|;
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|instance
operator|=
operator|new
name|MllpEndpoint
argument_list|(
literal|"mllp://dummy"
argument_list|,
operator|new
name|MllpComponent
argument_list|()
argument_list|,
operator|new
name|MllpConfiguration
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

