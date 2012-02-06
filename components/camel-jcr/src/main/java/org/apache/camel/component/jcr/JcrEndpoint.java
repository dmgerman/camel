begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jcr
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jcr
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URISyntaxException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jcr
operator|.
name|Credentials
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jcr
operator|.
name|Repository
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jcr
operator|.
name|SimpleCredentials
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
name|Consumer
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
name|Processor
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
name|Producer
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
name|RuntimeCamelException
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
name|impl
operator|.
name|DefaultEndpoint
import|;
end_import

begin_comment
comment|/**  * A JCR endpoint  */
end_comment

begin_class
DECL|class|JcrEndpoint
specifier|public
class|class
name|JcrEndpoint
extends|extends
name|DefaultEndpoint
block|{
DECL|field|credentials
specifier|private
name|Credentials
name|credentials
decl_stmt|;
DECL|field|repository
specifier|private
name|Repository
name|repository
decl_stmt|;
DECL|field|base
specifier|private
name|String
name|base
decl_stmt|;
DECL|method|JcrEndpoint (String endpointUri, JcrComponent component)
specifier|protected
name|JcrEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|JcrComponent
name|component
parameter_list|)
block|{
name|super
argument_list|(
name|endpointUri
argument_list|,
name|component
argument_list|)
expr_stmt|;
try|try
block|{
name|URI
name|uri
init|=
operator|new
name|URI
argument_list|(
name|endpointUri
argument_list|)
decl_stmt|;
if|if
condition|(
name|uri
operator|.
name|getUserInfo
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|String
index|[]
name|creds
init|=
name|uri
operator|.
name|getUserInfo
argument_list|()
operator|.
name|split
argument_list|(
literal|":"
argument_list|)
decl_stmt|;
if|if
condition|(
name|creds
operator|!=
literal|null
condition|)
block|{
name|String
name|username
init|=
name|creds
index|[
literal|0
index|]
decl_stmt|;
name|String
name|password
init|=
name|creds
operator|.
name|length
operator|>
literal|1
condition|?
name|creds
index|[
literal|1
index|]
else|:
literal|null
decl_stmt|;
name|this
operator|.
name|credentials
operator|=
operator|new
name|SimpleCredentials
argument_list|(
name|username
argument_list|,
name|password
operator|.
name|toCharArray
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|this
operator|.
name|repository
operator|=
name|component
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getRegistry
argument_list|()
operator|.
name|lookup
argument_list|(
name|uri
operator|.
name|getHost
argument_list|()
argument_list|,
name|Repository
operator|.
name|class
argument_list|)
expr_stmt|;
if|if
condition|(
name|repository
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
literal|"No JCR repository defined under '"
operator|+
name|uri
operator|.
name|getHost
argument_list|()
operator|+
literal|"'"
argument_list|)
throw|;
block|}
name|this
operator|.
name|base
operator|=
name|uri
operator|.
name|getPath
argument_list|()
operator|.
name|replaceAll
argument_list|(
literal|"^/"
argument_list|,
literal|""
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|URISyntaxException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Invalid URI: "
operator|+
name|endpointUri
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
comment|/**      * Currently unsupported      * @throws RuntimeCamelException      */
DECL|method|createConsumer (Processor processor)
specifier|public
name|Consumer
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
literal|"No consumer endpoint support for JCR available"
argument_list|)
throw|;
block|}
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|JcrProducer
argument_list|(
name|this
argument_list|)
return|;
block|}
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
comment|/**      * Get the {@link Repository}      *       * @return the repository      */
DECL|method|getRepository ()
specifier|protected
name|Repository
name|getRepository
parameter_list|()
block|{
return|return
name|repository
return|;
block|}
comment|/**      * Get the {@link Credentials} for establishing the JCR repository connection      *       * @return the credentials      */
DECL|method|getCredentials ()
specifier|protected
name|Credentials
name|getCredentials
parameter_list|()
block|{
return|return
name|credentials
return|;
block|}
comment|/**      * Get the base node when accessing the reposititory      *       * @return the base node      */
DECL|method|getBase ()
specifier|protected
name|String
name|getBase
parameter_list|()
block|{
return|return
name|base
return|;
block|}
block|}
end_class

end_unit

