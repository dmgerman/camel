begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|test
operator|.
name|junit4
operator|.
name|CamelTestSupport
import|;
end_import

begin_class
DECL|class|PGPKeyAccessDataFormatTest
specifier|public
class|class
name|PGPKeyAccessDataFormatTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
block|{
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|PGPPublicKeyAccessor
name|publicKeyAccessor
init|=
operator|new
name|DefaultPGPPublicKeyAccessor
argument_list|(
name|PGPDataFormatTest
operator|.
name|getPublicKeyRing
argument_list|()
argument_list|)
decl_stmt|;
name|PGPSecretKeyAccessor
name|secretKeyAccessor
init|=
operator|new
name|DefaultPGPSecretKeyAccessor
argument_list|(
name|PGPDataFormatTest
operator|.
name|getSecKeyRing
argument_list|()
argument_list|,
literal|"sdude"
argument_list|,
literal|"BC"
argument_list|)
decl_stmt|;
name|PGPKeyAccessDataFormat
name|dt
init|=
operator|new
name|PGPKeyAccessDataFormat
argument_list|()
decl_stmt|;
name|dt
operator|.
name|setPublicKeyAccessor
argument_list|(
name|publicKeyAccessor
argument_list|)
expr_stmt|;
name|dt
operator|.
name|setSecretKeyAccessor
argument_list|(
name|secretKeyAccessor
argument_list|)
expr_stmt|;
name|dt
operator|.
name|setKeyUserid
argument_list|(
literal|"sdude"
argument_list|)
expr_stmt|;
name|dt
operator|.
name|setSignatureKeyUserid
argument_list|(
literal|"sdude"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

