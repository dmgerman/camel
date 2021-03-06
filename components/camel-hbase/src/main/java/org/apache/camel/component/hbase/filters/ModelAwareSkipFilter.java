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
name|Filter
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
name|SkipFilter
import|;
end_import

begin_class
DECL|class|ModelAwareSkipFilter
specifier|public
class|class
name|ModelAwareSkipFilter
extends|extends
name|SkipFilter
implements|implements
name|ModelAwareFilter
argument_list|<
name|SkipFilter
argument_list|>
block|{
DECL|method|ModelAwareSkipFilter (Filter filter)
specifier|public
name|ModelAwareSkipFilter
parameter_list|(
name|Filter
name|filter
parameter_list|)
block|{
name|super
argument_list|(
name|filter
argument_list|)
expr_stmt|;
block|}
comment|/**      * Applies the message to {@link org.apache.hadoop.hbase.filter.Filter} to context.      */
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
if|if
condition|(
name|ModelAwareFilter
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|getFilter
argument_list|()
operator|.
name|getClass
argument_list|()
argument_list|)
condition|)
block|{
operator|(
operator|(
name|ModelAwareFilter
argument_list|<
name|?
argument_list|>
operator|)
name|getFilter
argument_list|()
operator|)
operator|.
name|apply
argument_list|(
name|context
argument_list|,
name|rowModel
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Wraps an existing {@link SkipFilter} filter into a {@link ModelAwareSkipFilter}.      */
DECL|method|wrap (SkipFilter filter)
specifier|public
specifier|static
name|ModelAwareSkipFilter
name|wrap
parameter_list|(
name|SkipFilter
name|filter
parameter_list|)
block|{
return|return
operator|new
name|ModelAwareSkipFilter
argument_list|(
name|filter
operator|.
name|getFilter
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getFilteredList ()
specifier|public
name|FilterList
name|getFilteredList
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

