begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.http.cloud
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|http
operator|.
name|cloud
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
name|impl
operator|.
name|cloud
operator|.
name|DefaultServiceCallExpression
import|;
end_import

begin_comment
comment|/**  * The default is based on camel-http, this class is added to allow further  * customizations.  */
end_comment

begin_class
DECL|class|HttpServiceExpression
specifier|public
specifier|final
class|class
name|HttpServiceExpression
extends|extends
name|DefaultServiceCallExpression
block|{
DECL|method|HttpServiceExpression ()
specifier|public
name|HttpServiceExpression
parameter_list|()
block|{     }
DECL|method|HttpServiceExpression (String hostHeader, String portHeader)
specifier|public
name|HttpServiceExpression
parameter_list|(
name|String
name|hostHeader
parameter_list|,
name|String
name|portHeader
parameter_list|)
block|{
name|super
argument_list|(
name|hostHeader
argument_list|,
name|portHeader
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

