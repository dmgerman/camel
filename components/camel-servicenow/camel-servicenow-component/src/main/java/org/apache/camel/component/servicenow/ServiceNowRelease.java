begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.servicenow
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
name|releases
operator|.
name|fuji
operator|.
name|FujiServiceNowProducer
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
name|releases
operator|.
name|helsinki
operator|.
name|HelsinkiServiceNowProducer
import|;
end_import

begin_enum
DECL|enum|ServiceNowRelease
specifier|public
enum|enum
name|ServiceNowRelease
implements|implements
name|ServiceNowProducerSupplier
block|{
DECL|enumConstant|FUJI
name|FUJI
block|{
annotation|@
name|Override
specifier|public
name|ServiceNowProducer
name|get
parameter_list|(
name|ServiceNowEndpoint
name|endpoint
parameter_list|)
throws|throws
name|Exception
block|{
return|return
operator|new
name|FujiServiceNowProducer
argument_list|(
name|endpoint
argument_list|)
return|;
block|}
block|}
block|,
DECL|enumConstant|GENEVA
name|GENEVA
block|{
annotation|@
name|Override
specifier|public
name|ServiceNowProducer
name|get
parameter_list|(
name|ServiceNowEndpoint
name|endpoint
parameter_list|)
throws|throws
name|Exception
block|{
return|return
literal|null
return|;
block|}
block|}
block|,
DECL|enumConstant|HELSINKI
name|HELSINKI
block|{
annotation|@
name|Override
specifier|public
name|ServiceNowProducer
name|get
parameter_list|(
name|ServiceNowEndpoint
name|endpoint
parameter_list|)
throws|throws
name|Exception
block|{
return|return
operator|new
name|HelsinkiServiceNowProducer
argument_list|(
name|endpoint
argument_list|)
return|;
block|}
block|}
block|}
end_enum

end_unit

