begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.micrometer.routepolicy
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|micrometer
operator|.
name|routepolicy
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
name|Route
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|micrometer
operator|.
name|MicrometerConstants
operator|.
name|DEFAULT_CAMEL_ROUTE_POLICY_METER_NAME
import|;
end_import

begin_comment
comment|/**  * Provides a strategy to derive a meter name from the route  */
end_comment

begin_interface
DECL|interface|MicrometerRoutePolicyNamingStrategy
specifier|public
interface|interface
name|MicrometerRoutePolicyNamingStrategy
block|{
DECL|field|DEFAULT
name|MicrometerRoutePolicyNamingStrategy
name|DEFAULT
init|=
name|route
lambda|->
name|DEFAULT_CAMEL_ROUTE_POLICY_METER_NAME
decl_stmt|;
DECL|method|getName (Route route)
name|String
name|getName
parameter_list|(
name|Route
name|route
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

