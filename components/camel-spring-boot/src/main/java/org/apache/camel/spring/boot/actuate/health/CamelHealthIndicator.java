begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring.boot.actuate.health
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|boot
operator|.
name|actuate
operator|.
name|health
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
name|CamelContext
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
name|spring
operator|.
name|boot
operator|.
name|health
operator|.
name|HealthCheckVerboseConfiguration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|actuate
operator|.
name|health
operator|.
name|AbstractHealthIndicator
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|actuate
operator|.
name|health
operator|.
name|Health
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|actuate
operator|.
name|health
operator|.
name|HealthIndicator
import|;
end_import

begin_comment
comment|/**  * Camel {@link HealthIndicator}.  */
end_comment

begin_class
DECL|class|CamelHealthIndicator
specifier|public
class|class
name|CamelHealthIndicator
extends|extends
name|AbstractHealthIndicator
block|{
DECL|field|camelContext
specifier|private
specifier|final
name|CamelContext
name|camelContext
decl_stmt|;
DECL|field|properties
specifier|private
specifier|final
name|HealthCheckVerboseConfiguration
name|properties
decl_stmt|;
DECL|method|CamelHealthIndicator (CamelContext camelContext, HealthCheckVerboseConfiguration properties)
specifier|public
name|CamelHealthIndicator
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|HealthCheckVerboseConfiguration
name|properties
parameter_list|)
block|{
name|this
operator|.
name|camelContext
operator|=
name|camelContext
expr_stmt|;
name|this
operator|.
name|properties
operator|=
name|properties
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doHealthCheck (Health.Builder builder)
specifier|protected
name|void
name|doHealthCheck
parameter_list|(
name|Health
operator|.
name|Builder
name|builder
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|camelContext
operator|==
literal|null
condition|)
block|{
name|builder
operator|.
name|unknown
argument_list|()
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|properties
operator|.
name|isVerbose
argument_list|()
condition|)
block|{
name|builder
operator|.
name|withDetail
argument_list|(
literal|"name"
argument_list|,
name|camelContext
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|builder
operator|.
name|withDetail
argument_list|(
literal|"version"
argument_list|,
name|camelContext
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|camelContext
operator|.
name|getUptime
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|withDetail
argument_list|(
literal|"uptime"
argument_list|,
name|camelContext
operator|.
name|getUptime
argument_list|()
argument_list|)
expr_stmt|;
name|builder
operator|.
name|withDetail
argument_list|(
literal|"uptimeMillis"
argument_list|,
name|camelContext
operator|.
name|getUptimeMillis
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|builder
operator|.
name|withDetail
argument_list|(
literal|"status"
argument_list|,
name|camelContext
operator|.
name|getStatus
argument_list|()
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|camelContext
operator|.
name|getStatus
argument_list|()
operator|.
name|isStarted
argument_list|()
condition|)
block|{
name|builder
operator|.
name|up
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|camelContext
operator|.
name|getStatus
argument_list|()
operator|.
name|isStopped
argument_list|()
condition|)
block|{
name|builder
operator|.
name|down
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|builder
operator|.
name|unknown
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

