begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.converter.crypto
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|converter
operator|.
name|crypto
package|;
end_package

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|Key
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|crypto
operator|.
name|KeyGenerator
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
name|builder
operator|.
name|RouteBuilder
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|processor
operator|.
name|SpringTestHelper
operator|.
name|createSpringCamelContext
import|;
end_import

begin_class
DECL|class|SpringCryptoDataFormatTest
specifier|public
class|class
name|SpringCryptoDataFormatTest
extends|extends
name|CryptoDataFormatTest
block|{
DECL|field|deskey
specifier|private
specifier|static
name|Key
name|deskey
decl_stmt|;
annotation|@
name|Override
DECL|method|createRouteBuilders ()
specifier|protected
name|RouteBuilder
index|[]
name|createRouteBuilders
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|RouteBuilder
index|[]
block|{}
return|;
block|}
DECL|method|createCamelContext ()
specifier|protected
name|CamelContext
name|createCamelContext
parameter_list|()
throws|throws
name|Exception
block|{
name|KeyGenerator
name|generator
init|=
name|KeyGenerator
operator|.
name|getInstance
argument_list|(
literal|"DES"
argument_list|)
decl_stmt|;
name|deskey
operator|=
name|generator
operator|.
name|generateKey
argument_list|()
expr_stmt|;
return|return
name|createSpringCamelContext
argument_list|(
name|this
argument_list|,
literal|"/org/apache/camel/component/crypto/SpringCryptoDataFormatTest.xml"
argument_list|)
return|;
block|}
DECL|method|getDesKey ()
specifier|public
specifier|static
name|Key
name|getDesKey
parameter_list|()
block|{
return|return
name|deskey
return|;
block|}
DECL|method|getIV ()
specifier|public
specifier|static
name|byte
index|[]
name|getIV
parameter_list|()
block|{
return|return
operator|new
name|byte
index|[]
block|{
literal|0x00
block|,
literal|0x01
block|,
literal|0x02
block|,
literal|0x03
block|,
literal|0x04
block|,
literal|0x05
block|,
literal|0x06
block|,
literal|0x07
block|}
return|;
block|}
block|}
end_class

end_unit

