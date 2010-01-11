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
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedHashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
name|Header
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
name|model
operator|.
name|fix
operator|.
name|complex
operator|.
name|onetomany
operator|.
name|Order
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
name|model
operator|.
name|fix
operator|.
name|complex
operator|.
name|onetomany
operator|.
name|Security
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
name|model
operator|.
name|fix
operator|.
name|complex
operator|.
name|onetomany
operator|.
name|Trailer
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
name|result
operator|.
name|expectedBodiesReceived
argument_list|(
name|generateModel
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|result
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|method|generateModel ()
specifier|public
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
name|generateModel
parameter_list|()
block|{
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
name|models
init|=
operator|new
name|ArrayList
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
comment|// must use linked to preserve order
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|model
init|=
operator|new
name|LinkedHashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Security
argument_list|>
name|securities
init|=
operator|new
name|ArrayList
argument_list|<
name|Security
argument_list|>
argument_list|()
decl_stmt|;
name|Header
name|header
init|=
operator|new
name|Header
argument_list|()
decl_stmt|;
name|header
operator|.
name|setBeginString
argument_list|(
literal|"FIX 4.1"
argument_list|)
expr_stmt|;
name|header
operator|.
name|setBodyLength
argument_list|(
literal|20
argument_list|)
expr_stmt|;
name|header
operator|.
name|setMsgSeqNum
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|header
operator|.
name|setMsgType
argument_list|(
literal|"0"
argument_list|)
expr_stmt|;
name|header
operator|.
name|setSendCompId
argument_list|(
literal|"INVMGR"
argument_list|)
expr_stmt|;
name|header
operator|.
name|setTargetCompId
argument_list|(
literal|"BRKR"
argument_list|)
expr_stmt|;
name|Trailer
name|trailer
init|=
operator|new
name|Trailer
argument_list|()
decl_stmt|;
name|trailer
operator|.
name|setCheckSum
argument_list|(
literal|220
argument_list|)
expr_stmt|;
name|Order
name|order
init|=
operator|new
name|Order
argument_list|()
decl_stmt|;
name|order
operator|.
name|setAccount
argument_list|(
literal|"BE.CHM.001"
argument_list|)
expr_stmt|;
name|order
operator|.
name|setClOrdId
argument_list|(
literal|"CHM0001-01"
argument_list|)
expr_stmt|;
name|order
operator|.
name|setText
argument_list|(
literal|"this is a camel - bindy test"
argument_list|)
expr_stmt|;
comment|// 1st security
name|Security
name|security
init|=
operator|new
name|Security
argument_list|()
decl_stmt|;
name|security
operator|.
name|setIdSource
argument_list|(
literal|"4"
argument_list|)
expr_stmt|;
name|security
operator|.
name|setSecurityCode
argument_list|(
literal|"BE0001245678"
argument_list|)
expr_stmt|;
name|security
operator|.
name|setSide
argument_list|(
literal|"1"
argument_list|)
expr_stmt|;
name|securities
operator|.
name|add
argument_list|(
name|security
argument_list|)
expr_stmt|;
comment|// 2nd security
name|security
operator|=
operator|new
name|Security
argument_list|()
expr_stmt|;
name|security
operator|.
name|setIdSource
argument_list|(
literal|"5"
argument_list|)
expr_stmt|;
name|security
operator|.
name|setSecurityCode
argument_list|(
literal|"BE0009876543"
argument_list|)
expr_stmt|;
name|security
operator|.
name|setSide
argument_list|(
literal|"2"
argument_list|)
expr_stmt|;
name|securities
operator|.
name|add
argument_list|(
name|security
argument_list|)
expr_stmt|;
comment|// 3rd security
name|security
operator|=
operator|new
name|Security
argument_list|()
expr_stmt|;
name|security
operator|.
name|setIdSource
argument_list|(
literal|"6"
argument_list|)
expr_stmt|;
name|security
operator|.
name|setSecurityCode
argument_list|(
literal|"BE0009999999"
argument_list|)
expr_stmt|;
name|security
operator|.
name|setSide
argument_list|(
literal|"3"
argument_list|)
expr_stmt|;
name|securities
operator|.
name|add
argument_list|(
name|security
argument_list|)
expr_stmt|;
name|order
operator|.
name|setSecurities
argument_list|(
name|securities
argument_list|)
expr_stmt|;
name|order
operator|.
name|setHeader
argument_list|(
name|header
argument_list|)
expr_stmt|;
name|order
operator|.
name|setTrailer
argument_list|(
name|trailer
argument_list|)
expr_stmt|;
name|model
operator|.
name|put
argument_list|(
name|order
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|order
argument_list|)
expr_stmt|;
name|model
operator|.
name|put
argument_list|(
name|header
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|header
argument_list|)
expr_stmt|;
name|model
operator|.
name|put
argument_list|(
name|trailer
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|trailer
argument_list|)
expr_stmt|;
name|models
operator|.
name|add
argument_list|(
name|model
argument_list|)
expr_stmt|;
return|return
name|models
return|;
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
literal|"org.apache.camel.dataformat.bindy.model.fix.complex.onetomany"
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

