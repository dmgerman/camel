begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.dataformat.bindy.fix
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|dataformat
operator|.
name|bindy
operator|.
name|fix
package|;
end_package

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
name|dataformat
operator|.
name|bindy
operator|.
name|CommonBindyTest
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
name|dataformat
operator|.
name|bindy
operator|.
name|kvp
operator|.
name|BindyKeyValuePairDataFormat
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
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

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|test
operator|.
name|annotation
operator|.
name|DirtiesContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|test
operator|.
name|context
operator|.
name|ContextConfiguration
import|;
end_import

begin_class
annotation|@
name|ContextConfiguration
DECL|class|BindyComplexOneToManyKeyValuePairUnMarshallTest
specifier|public
class|class
name|BindyComplexOneToManyKeyValuePairUnMarshallTest
extends|extends
name|CommonBindyTest
block|{
annotation|@
name|Test
annotation|@
name|DirtiesContext
DECL|method|testUnMarshallMessage ()
specifier|public
name|void
name|testUnMarshallMessage
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|message
init|=
literal|"8=FIX 4.1
literal|9=20
literal|34=1
literal|35=0
literal|49=INVMGR
literal|56=BRKR"
operator|+
literal|"
literal|1=BE.CHM.001
literal|11=CHM0001-01
literal|58=this is a camel - bindy test"
operator|+
literal|"
literal|22=4
literal|48=BE0001245678
literal|54=1"
operator|+
literal|"
literal|22=5
literal|48=BE0009876543
literal|54=2"
operator|+
literal|"
literal|22=6
literal|48=BE0009999999
literal|54=3"
operator|+
literal|"
literal|10=220"
decl_stmt|;
name|String
name|message2
init|=
literal|"8=FIX 4.1
literal|9=20
literal|34=1
literal|35=0
literal|49=INVMGR
literal|56=BRKR"
operator|+
literal|"
literal|1=BE.CHM.001
literal|11=CHM0001-01
literal|58=this is a camel - bindy test
literal|10=220"
decl_stmt|;
name|result
operator|.
name|expectedMessageCount
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
name|message2
argument_list|)
expr_stmt|;
name|result
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|String
name|body
init|=
name|result
operator|.
name|getReceivedExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|body
operator|.
name|contains
argument_list|(
literal|"BE.CHM.001, 11: CHM0001-01, 58: this is a camel - bindy test"
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|body
operator|.
name|contains
argument_list|(
literal|"22: 4, 48: BE0001245678, 54: 1"
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|body
operator|.
name|contains
argument_list|(
literal|"22: 5, 48: BE0009876543, 54: 2"
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|body
operator|.
name|contains
argument_list|(
literal|"22: 6, 48: BE0009999999, 54: 3"
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|body
operator|.
name|contains
argument_list|(
literal|"FIX 4.1, 9: 20, 34: 1 , 35: 0, 49: INVMGR, 56: BRKR"
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|body
operator|.
name|contains
argument_list|(
literal|"10: 220"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|class|ContextConfig
specifier|public
specifier|static
class|class
name|ContextConfig
extends|extends
name|RouteBuilder
block|{
DECL|field|kvpBindyDataFormat
name|BindyKeyValuePairDataFormat
name|kvpBindyDataFormat
init|=
operator|new
name|BindyKeyValuePairDataFormat
argument_list|(
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|dataformat
operator|.
name|bindy
operator|.
name|model
operator|.
name|fix
operator|.
name|complex
operator|.
name|onetomany
operator|.
name|Order
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|configure ()
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
name|URI_DIRECT_START
argument_list|)
operator|.
name|unmarshal
argument_list|(
name|kvpBindyDataFormat
argument_list|)
operator|.
name|to
argument_list|(
name|URI_MOCK_RESULT
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

