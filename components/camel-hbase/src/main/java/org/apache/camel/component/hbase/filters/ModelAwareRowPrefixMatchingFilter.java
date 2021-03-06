begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.hbase.filters
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|hbase
operator|.
name|filters
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
name|component
operator|.
name|hbase
operator|.
name|model
operator|.
name|HBaseRow
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hadoop
operator|.
name|hbase
operator|.
name|filter
operator|.
name|FilterList
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hadoop
operator|.
name|hbase
operator|.
name|filter
operator|.
name|PrefixFilter
import|;
end_import

begin_comment
comment|/**  * A {@link FilterList} that contains multiple {@link PrefixFilter}s one per column that is part of the model.  */
end_comment

begin_class
DECL|class|ModelAwareRowPrefixMatchingFilter
specifier|public
class|class
name|ModelAwareRowPrefixMatchingFilter
extends|extends
name|ModelAwareFilterList
block|{
comment|/**      * Writable constructor, do not use.      */
DECL|method|ModelAwareRowPrefixMatchingFilter ()
specifier|public
name|ModelAwareRowPrefixMatchingFilter
parameter_list|()
block|{     }
comment|/**      * Applies the message to {@link org.apache.hadoop.hbase.filter.Filter} to      * context.      */
annotation|@
name|Override
DECL|method|apply (CamelContext context, HBaseRow rowModel)
specifier|public
name|void
name|apply
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|HBaseRow
name|rowModel
parameter_list|)
block|{
name|getFilters
argument_list|()
operator|.
name|clear
argument_list|()
expr_stmt|;
if|if
condition|(
name|rowModel
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|rowModel
operator|.
name|getId
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|byte
index|[]
name|value
init|=
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|byte
index|[]
operator|.
expr|class
argument_list|,
name|rowModel
operator|.
name|getId
argument_list|()
argument_list|)
decl_stmt|;
name|PrefixFilter
name|rowPrefixFilter
init|=
operator|new
name|PrefixFilter
argument_list|(
name|value
argument_list|)
decl_stmt|;
name|addFilter
argument_list|(
name|rowPrefixFilter
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

