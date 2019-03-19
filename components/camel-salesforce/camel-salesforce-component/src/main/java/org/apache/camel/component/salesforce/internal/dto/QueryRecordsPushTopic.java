begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.salesforce.internal.dto
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|salesforce
operator|.
name|internal
operator|.
name|dto
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|com
operator|.
name|thoughtworks
operator|.
name|xstream
operator|.
name|annotations
operator|.
name|XStreamImplicit
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
name|salesforce
operator|.
name|api
operator|.
name|dto
operator|.
name|AbstractQueryRecordsBase
import|;
end_import

begin_comment
comment|/**  * Salesforce Query Records DTO for PushTopic  */
end_comment

begin_class
DECL|class|QueryRecordsPushTopic
specifier|public
class|class
name|QueryRecordsPushTopic
extends|extends
name|AbstractQueryRecordsBase
block|{
annotation|@
name|XStreamImplicit
DECL|field|records
specifier|private
name|List
argument_list|<
name|PushTopic
argument_list|>
name|records
decl_stmt|;
DECL|method|getRecords ()
specifier|public
name|List
argument_list|<
name|PushTopic
argument_list|>
name|getRecords
parameter_list|()
block|{
return|return
name|records
return|;
block|}
DECL|method|setRecords (List<PushTopic> records)
specifier|public
name|void
name|setRecords
parameter_list|(
name|List
argument_list|<
name|PushTopic
argument_list|>
name|records
parameter_list|)
block|{
name|this
operator|.
name|records
operator|=
name|records
expr_stmt|;
block|}
block|}
end_class

end_unit

