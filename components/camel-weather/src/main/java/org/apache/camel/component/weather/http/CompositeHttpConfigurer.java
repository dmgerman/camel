begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.weather.http
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|weather
operator|.
name|http
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
name|Collections
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
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|httpclient
operator|.
name|HttpClient
import|;
end_import

begin_class
DECL|class|CompositeHttpConfigurer
specifier|public
class|class
name|CompositeHttpConfigurer
implements|implements
name|HttpClientConfigurer
block|{
DECL|field|configurers
specifier|private
specifier|final
name|List
argument_list|<
name|HttpClientConfigurer
argument_list|>
name|configurers
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
DECL|method|addConfigurer (HttpClientConfigurer configurer)
specifier|public
name|void
name|addConfigurer
parameter_list|(
name|HttpClientConfigurer
name|configurer
parameter_list|)
block|{
if|if
condition|(
name|configurer
operator|!=
literal|null
condition|)
block|{
name|configurers
operator|.
name|add
argument_list|(
name|configurer
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|removeConfigurer (HttpClientConfigurer configurer)
specifier|public
name|void
name|removeConfigurer
parameter_list|(
name|HttpClientConfigurer
name|configurer
parameter_list|)
block|{
name|configurers
operator|.
name|remove
argument_list|(
name|configurer
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|configureHttpClient (HttpClient client)
specifier|public
name|HttpClient
name|configureHttpClient
parameter_list|(
name|HttpClient
name|client
parameter_list|)
block|{
for|for
control|(
name|HttpClientConfigurer
name|configurer
range|:
name|configurers
control|)
block|{
name|configurer
operator|.
name|configureHttpClient
argument_list|(
name|client
argument_list|)
expr_stmt|;
block|}
return|return
name|client
return|;
block|}
DECL|method|getConfigurers ()
specifier|public
name|List
argument_list|<
name|HttpClientConfigurer
argument_list|>
name|getConfigurers
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|unmodifiableList
argument_list|(
name|configurers
argument_list|)
return|;
block|}
block|}
end_class

end_unit

