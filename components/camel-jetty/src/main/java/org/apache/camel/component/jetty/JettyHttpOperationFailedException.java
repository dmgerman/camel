begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jetty
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jetty
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
name|CamelException
import|;
end_import

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|JettyHttpOperationFailedException
specifier|public
class|class
name|JettyHttpOperationFailedException
extends|extends
name|CamelException
block|{
DECL|field|url
specifier|private
specifier|final
name|String
name|url
decl_stmt|;
DECL|field|statusCode
specifier|private
specifier|final
name|int
name|statusCode
decl_stmt|;
DECL|field|responseBody
specifier|private
specifier|final
name|String
name|responseBody
decl_stmt|;
DECL|method|JettyHttpOperationFailedException (String url, int statusCode, String responseBody)
specifier|public
name|JettyHttpOperationFailedException
parameter_list|(
name|String
name|url
parameter_list|,
name|int
name|statusCode
parameter_list|,
name|String
name|responseBody
parameter_list|)
block|{
name|super
argument_list|(
literal|"HTTP operation failed invoking "
operator|+
name|url
operator|+
literal|" with statusCode: "
operator|+
name|statusCode
argument_list|)
expr_stmt|;
name|this
operator|.
name|statusCode
operator|=
name|statusCode
expr_stmt|;
name|this
operator|.
name|url
operator|=
name|url
expr_stmt|;
name|this
operator|.
name|responseBody
operator|=
name|responseBody
expr_stmt|;
block|}
DECL|method|isRedirectError ()
specifier|public
name|boolean
name|isRedirectError
parameter_list|()
block|{
return|return
name|statusCode
operator|>=
literal|300
operator|&&
name|statusCode
operator|<
literal|400
return|;
block|}
DECL|method|getUrl ()
specifier|public
name|String
name|getUrl
parameter_list|()
block|{
return|return
name|url
return|;
block|}
DECL|method|getStatusCode ()
specifier|public
name|int
name|getStatusCode
parameter_list|()
block|{
return|return
name|statusCode
return|;
block|}
DECL|method|getResponseBody ()
specifier|public
name|String
name|getResponseBody
parameter_list|()
block|{
return|return
name|responseBody
return|;
block|}
block|}
end_class

end_unit

