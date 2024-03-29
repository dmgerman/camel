begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.http
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
name|List
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|impl
operator|.
name|client
operator|.
name|HttpClientBuilder
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
DECL|method|configureHttpClient (HttpClientBuilder clientBuilder)
specifier|public
name|void
name|configureHttpClient
parameter_list|(
name|HttpClientBuilder
name|clientBuilder
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
name|clientBuilder
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|combineConfigurers (HttpClientConfigurer oldConfigurer, HttpClientConfigurer newConfigurer)
specifier|public
specifier|static
name|CompositeHttpConfigurer
name|combineConfigurers
parameter_list|(
name|HttpClientConfigurer
name|oldConfigurer
parameter_list|,
name|HttpClientConfigurer
name|newConfigurer
parameter_list|)
block|{
if|if
condition|(
name|oldConfigurer
operator|instanceof
name|CompositeHttpConfigurer
condition|)
block|{
operator|(
operator|(
name|CompositeHttpConfigurer
operator|)
name|oldConfigurer
operator|)
operator|.
name|addConfigurer
argument_list|(
name|newConfigurer
argument_list|)
expr_stmt|;
return|return
operator|(
name|CompositeHttpConfigurer
operator|)
name|oldConfigurer
return|;
block|}
else|else
block|{
name|CompositeHttpConfigurer
name|answer
init|=
operator|new
name|CompositeHttpConfigurer
argument_list|()
decl_stmt|;
name|answer
operator|.
name|addConfigurer
argument_list|(
name|newConfigurer
argument_list|)
expr_stmt|;
name|answer
operator|.
name|addConfigurer
argument_list|(
name|oldConfigurer
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
block|}
block|}
end_class

end_unit

