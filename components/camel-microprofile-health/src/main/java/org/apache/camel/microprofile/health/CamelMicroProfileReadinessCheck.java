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
name|org
operator|.
name|eclipse
operator|.
name|microprofile
operator|.
name|health
operator|.
name|Readiness
import|;
end_import

begin_class
annotation|@
name|Readiness
DECL|class|CamelMicroProfileReadinessCheck
specifier|public
class|class
name|CamelMicroProfileReadinessCheck
extends|extends
name|AbstractCamelMicroProfileHealthCheck
block|{
annotation|@
name|Override
DECL|method|getHealthGroupFilterExclude ()
name|String
name|getHealthGroupFilterExclude
parameter_list|()
block|{
return|return
name|AbstractCamelMicroProfileLivenessCheck
operator|.
name|HEALTH_GROUP_LIVENESS
return|;
block|}
annotation|@
name|Override
DECL|method|getHealthCheckName ()
name|String
name|getHealthCheckName
parameter_list|()
block|{
return|return
literal|"camel-readiness-checks"
return|;
block|}
block|}
end_class

end_unit

