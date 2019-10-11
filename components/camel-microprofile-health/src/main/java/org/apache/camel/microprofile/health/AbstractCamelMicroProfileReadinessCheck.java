begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.microprofile.health
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|microprofile
operator|.
name|health
package|;
end_package

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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
operator|.
name|health
operator|.
name|AbstractHealthCheck
import|;
end_import

begin_comment
comment|/**  * Ensures the implemented health check will be considered as a MicroProfile Health readiness check  */
end_comment

begin_class
DECL|class|AbstractCamelMicroProfileReadinessCheck
specifier|public
specifier|abstract
class|class
name|AbstractCamelMicroProfileReadinessCheck
extends|extends
name|AbstractHealthCheck
block|{
DECL|field|HEALTH_GROUP_READINESS
specifier|public
specifier|static
specifier|final
name|String
name|HEALTH_GROUP_READINESS
init|=
literal|"camel.health.readiness"
decl_stmt|;
DECL|method|AbstractCamelMicroProfileReadinessCheck (String id)
specifier|public
name|AbstractCamelMicroProfileReadinessCheck
parameter_list|(
name|String
name|id
parameter_list|)
block|{
name|super
argument_list|(
name|HEALTH_GROUP_READINESS
argument_list|,
name|id
argument_list|)
expr_stmt|;
block|}
DECL|method|AbstractCamelMicroProfileReadinessCheck (String id, Map<String, Object> meta)
specifier|public
name|AbstractCamelMicroProfileReadinessCheck
parameter_list|(
name|String
name|id
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|meta
parameter_list|)
block|{
name|super
argument_list|(
name|HEALTH_GROUP_READINESS
argument_list|,
name|id
argument_list|,
name|meta
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

