begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.hl7
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|hl7
package|;
end_package

begin_import
import|import
name|ca
operator|.
name|uhn
operator|.
name|hl7v2
operator|.
name|AcknowledgmentCode
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
name|Test
import|;
end_import

begin_class
DECL|class|AckCodeTest
specifier|public
class|class
name|AckCodeTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|Test
DECL|method|verifyAcknowledgmentCodeConversion ()
specifier|public
name|void
name|verifyAcknowledgmentCodeConversion
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"Not the expected same count of enum members"
argument_list|,
name|AckCode
operator|.
name|values
argument_list|()
operator|.
name|length
argument_list|,
name|AcknowledgmentCode
operator|.
name|values
argument_list|()
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
literal|"Not the expect 'Application Accept' enum member"
argument_list|,
name|AcknowledgmentCode
operator|.
name|AA
argument_list|,
name|AckCode
operator|.
name|AA
operator|.
name|asAcknowledgmentCode
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
literal|"Not the expect 'Application Error' enum member"
argument_list|,
name|AcknowledgmentCode
operator|.
name|AE
argument_list|,
name|AckCode
operator|.
name|AE
operator|.
name|asAcknowledgmentCode
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
literal|"Not the expect 'Application Reject' enum member"
argument_list|,
name|AcknowledgmentCode
operator|.
name|AR
argument_list|,
name|AckCode
operator|.
name|AR
operator|.
name|asAcknowledgmentCode
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
literal|"Not the expect 'Commit Accept' enum member"
argument_list|,
name|AcknowledgmentCode
operator|.
name|CA
argument_list|,
name|AckCode
operator|.
name|CA
operator|.
name|asAcknowledgmentCode
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
literal|"Not the expect 'Commit Error' enum member"
argument_list|,
name|AcknowledgmentCode
operator|.
name|CE
argument_list|,
name|AckCode
operator|.
name|CE
operator|.
name|asAcknowledgmentCode
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
literal|"Not the expect 'Commit Reject' enum member"
argument_list|,
name|AcknowledgmentCode
operator|.
name|CR
argument_list|,
name|AckCode
operator|.
name|CR
operator|.
name|asAcknowledgmentCode
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

