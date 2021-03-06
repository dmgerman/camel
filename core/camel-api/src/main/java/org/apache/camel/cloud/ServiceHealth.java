begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.cloud
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|cloud
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
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Optional
import|;
end_import

begin_interface
DECL|interface|ServiceHealth
specifier|public
interface|interface
name|ServiceHealth
block|{
comment|/**      * Gets a key/value metadata associated with the service.      */
DECL|method|getMetadata ()
specifier|default
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getMetadata
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|EMPTY_MAP
return|;
block|}
comment|/**      * States if the service is healthy or not      */
DECL|method|isHealthy ()
specifier|default
name|boolean
name|isHealthy
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
comment|/**      * The health enpoint exposed by the service.      */
DECL|method|getEndpoint ()
specifier|default
name|Optional
argument_list|<
name|URI
argument_list|>
name|getEndpoint
parameter_list|()
block|{
return|return
name|Optional
operator|.
name|empty
argument_list|()
return|;
block|}
block|}
end_interface

end_unit

