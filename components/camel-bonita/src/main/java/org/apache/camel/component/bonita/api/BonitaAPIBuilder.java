begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.bonita.api
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|bonita
operator|.
name|api
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|client
operator|.
name|Client
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|client
operator|.
name|ClientBuilder
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|client
operator|.
name|WebTarget
import|;
end_import

begin_import
import|import
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|jaxrs
operator|.
name|json
operator|.
name|JacksonJsonProvider
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
name|component
operator|.
name|bonita
operator|.
name|api
operator|.
name|filter
operator|.
name|BonitaAuthFilter
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
name|component
operator|.
name|bonita
operator|.
name|api
operator|.
name|util
operator|.
name|BonitaAPIConfig
import|;
end_import

begin_class
DECL|class|BonitaAPIBuilder
specifier|public
class|class
name|BonitaAPIBuilder
block|{
DECL|method|BonitaAPIBuilder ()
specifier|protected
name|BonitaAPIBuilder
parameter_list|()
block|{      }
DECL|method|build (BonitaAPIConfig bonitaAPIConfig)
specifier|public
specifier|static
name|BonitaAPI
name|build
parameter_list|(
name|BonitaAPIConfig
name|bonitaAPIConfig
parameter_list|)
block|{
if|if
condition|(
name|bonitaAPIConfig
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"bonitaApiConfig is null"
argument_list|)
throw|;
block|}
name|ClientBuilder
name|clientBuilder
init|=
name|ClientBuilder
operator|.
name|newBuilder
argument_list|()
decl_stmt|;
name|clientBuilder
operator|.
name|register
argument_list|(
name|JacksonJsonProvider
operator|.
name|class
argument_list|)
expr_stmt|;
name|Client
name|client
init|=
name|clientBuilder
operator|.
name|build
argument_list|()
decl_stmt|;
name|client
operator|.
name|register
argument_list|(
operator|new
name|BonitaAuthFilter
argument_list|(
name|bonitaAPIConfig
argument_list|)
argument_list|)
expr_stmt|;
name|WebTarget
name|webTarget
init|=
name|client
operator|.
name|target
argument_list|(
name|bonitaAPIConfig
operator|.
name|getBaseBonitaURI
argument_list|()
argument_list|)
operator|.
name|path
argument_list|(
literal|"/API/bpm"
argument_list|)
decl_stmt|;
return|return
operator|new
name|BonitaAPI
argument_list|(
name|bonitaAPIConfig
argument_list|,
name|webTarget
argument_list|)
return|;
block|}
block|}
end_class

end_unit

