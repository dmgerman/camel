begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.servicenow.releases.fuji
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|servicenow
operator|.
name|releases
operator|.
name|fuji
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
name|component
operator|.
name|servicenow
operator|.
name|AbstractServiceNowProducer
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
name|servicenow
operator|.
name|ServiceNowConstants
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
name|servicenow
operator|.
name|ServiceNowEndpoint
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
name|servicenow
operator|.
name|ServiceNowRelease
import|;
end_import

begin_comment
comment|/**  * The Fuji ServiceNow producer.  */
end_comment

begin_class
DECL|class|FujiServiceNowProducer
specifier|public
class|class
name|FujiServiceNowProducer
extends|extends
name|AbstractServiceNowProducer
block|{
DECL|method|FujiServiceNowProducer (ServiceNowEndpoint endpoint)
specifier|public
name|FujiServiceNowProducer
parameter_list|(
name|ServiceNowEndpoint
name|endpoint
parameter_list|)
throws|throws
name|Exception
block|{
name|super
argument_list|(
name|endpoint
argument_list|,
name|ServiceNowRelease
operator|.
name|FUJI
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|ServiceNowConstants
operator|.
name|RESOURCE_TABLE
argument_list|,
operator|new
name|FujiServiceNowTableProcessor
argument_list|(
name|endpoint
argument_list|)
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|ServiceNowConstants
operator|.
name|RESOURCE_AGGREGATE
argument_list|,
operator|new
name|FujiServiceNowAggregateProcessor
argument_list|(
name|endpoint
argument_list|)
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|ServiceNowConstants
operator|.
name|RESOURCE_IMPORT
argument_list|,
operator|new
name|FujiServiceNowImportSetProcessor
argument_list|(
name|endpoint
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

