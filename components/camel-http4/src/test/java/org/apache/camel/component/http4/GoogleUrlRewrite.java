begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.http4
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|http4
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
name|Producer
import|;
end_import

begin_comment
comment|// START SNIPPET: e1
end_comment

begin_comment
comment|/**  * A very simple url rewrite that replaces yahoo with google in the url.  *<p/>  * This is only used for testing purposes.  */
end_comment

begin_class
DECL|class|GoogleUrlRewrite
specifier|public
class|class
name|GoogleUrlRewrite
implements|implements
name|UrlRewrite
block|{
annotation|@
name|Override
DECL|method|rewrite (String url, String relativeUrl, Producer producer)
specifier|public
name|String
name|rewrite
parameter_list|(
name|String
name|url
parameter_list|,
name|String
name|relativeUrl
parameter_list|,
name|Producer
name|producer
parameter_list|)
block|{
return|return
name|url
operator|.
name|replaceAll
argument_list|(
literal|"yahoo"
argument_list|,
literal|"google"
argument_list|)
return|;
block|}
block|}
end_class

begin_comment
comment|// END SNIPPET: e1
end_comment

end_unit

