begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel
package|package
name|org
operator|.
name|apache
operator|.
name|camel
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
name|support
operator|.
name|jsse
operator|.
name|SSLContextParameters
import|;
end_import

begin_comment
comment|/**  * Indicates that an object is able to use the global {@link SSLContextParameters} if configured.  */
end_comment

begin_interface
DECL|interface|SSLContextParametersAware
specifier|public
interface|interface
name|SSLContextParametersAware
extends|extends
name|CamelContextAware
block|{
comment|/**      * Returns the global {@link SSLContextParameters} if enabled on the implementing object, null otherwise.      */
DECL|method|retrieveGlobalSslContextParameters ()
specifier|default
name|SSLContextParameters
name|retrieveGlobalSslContextParameters
parameter_list|()
block|{
if|if
condition|(
name|isUseGlobalSslContextParameters
argument_list|()
condition|)
block|{
return|return
name|getCamelContext
argument_list|()
operator|.
name|getSSLContextParameters
argument_list|()
return|;
block|}
return|return
literal|null
return|;
block|}
comment|/**      * Determine if the implementing object is using global SSL context parameters.      */
DECL|method|isUseGlobalSslContextParameters ()
name|boolean
name|isUseGlobalSslContextParameters
parameter_list|()
function_decl|;
comment|/**      * Enable usage of global SSL context parameters.      */
DECL|method|setUseGlobalSslContextParameters (boolean useGlobalSslContextParameters)
name|void
name|setUseGlobalSslContextParameters
parameter_list|(
name|boolean
name|useGlobalSslContextParameters
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

