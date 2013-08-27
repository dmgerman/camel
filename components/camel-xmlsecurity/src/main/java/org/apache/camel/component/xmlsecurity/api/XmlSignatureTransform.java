begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.xmlsecurity.api
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|xmlsecurity
operator|.
name|api
package|;
end_package

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|spec
operator|.
name|AlgorithmParameterSpec
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|crypto
operator|.
name|AlgorithmMethod
import|;
end_import

begin_comment
comment|/**  * Transform and canonicalization algorithms with their parameters.  */
end_comment

begin_class
DECL|class|XmlSignatureTransform
specifier|public
class|class
name|XmlSignatureTransform
implements|implements
name|AlgorithmMethod
block|{
DECL|field|algorithm
specifier|private
name|String
name|algorithm
decl_stmt|;
DECL|field|parameterSpec
specifier|private
name|AlgorithmParameterSpec
name|parameterSpec
decl_stmt|;
DECL|method|XmlSignatureTransform ()
specifier|public
name|XmlSignatureTransform
parameter_list|()
block|{      }
DECL|method|XmlSignatureTransform (String algorithm)
specifier|public
name|XmlSignatureTransform
parameter_list|(
name|String
name|algorithm
parameter_list|)
block|{
name|this
operator|.
name|algorithm
operator|=
name|algorithm
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getParameterSpec ()
specifier|public
name|AlgorithmParameterSpec
name|getParameterSpec
parameter_list|()
block|{
return|return
name|parameterSpec
return|;
block|}
annotation|@
name|Override
DECL|method|getAlgorithm ()
specifier|public
name|String
name|getAlgorithm
parameter_list|()
block|{
return|return
name|algorithm
return|;
block|}
DECL|method|setAlgorithm (String algorithm)
specifier|public
name|void
name|setAlgorithm
parameter_list|(
name|String
name|algorithm
parameter_list|)
block|{
name|this
operator|.
name|algorithm
operator|=
name|algorithm
expr_stmt|;
block|}
DECL|method|setParameterSpec (AlgorithmParameterSpec parameterSpec)
specifier|public
name|void
name|setParameterSpec
parameter_list|(
name|AlgorithmParameterSpec
name|parameterSpec
parameter_list|)
block|{
name|this
operator|.
name|parameterSpec
operator|=
name|parameterSpec
expr_stmt|;
block|}
block|}
end_class

end_unit

