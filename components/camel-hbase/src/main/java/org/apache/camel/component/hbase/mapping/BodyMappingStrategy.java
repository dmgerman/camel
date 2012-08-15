begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.hbase.mapping
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
name|mapping
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
name|Message
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
name|HBaseData
import|;
end_import

begin_comment
comment|/**  * A {@link org.apache.camel.component.hbase.mapping.CellMappingStrategy} implementation.  * It distinguishes between multiple cell, by reading headers with index suffix.  *<p/>  * In case of multiple headers:  *<ul>  *<li>First header is expected to have no suffix</li>  *<li>Suffixes start from number 2</li>  *<li>Suffixes need to be sequential</li>  *</ul>  */
end_comment

begin_class
DECL|class|BodyMappingStrategy
specifier|public
class|class
name|BodyMappingStrategy
implements|implements
name|CellMappingStrategy
block|{
comment|/**      * Resolves the cells that the {@link org.apache.camel.Exchange} refers to.      */
annotation|@
name|Override
DECL|method|resolveModel (Message message)
specifier|public
name|HBaseData
name|resolveModel
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
return|return
name|message
operator|.
name|getBody
argument_list|(
name|HBaseData
operator|.
name|class
argument_list|)
return|;
block|}
comment|/**      * Applies the cells to the {@link org.apache.camel.Exchange}.      */
DECL|method|applyGetResults (Message message, HBaseData data)
specifier|public
name|void
name|applyGetResults
parameter_list|(
name|Message
name|message
parameter_list|,
name|HBaseData
name|data
parameter_list|)
block|{
if|if
condition|(
name|data
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|message
operator|.
name|setBody
argument_list|(
name|data
argument_list|)
expr_stmt|;
block|}
comment|/**      * Applies the cells to the {@link org.apache.camel.Exchange}.      */
DECL|method|applyScanResults (Message message, HBaseData data)
specifier|public
name|void
name|applyScanResults
parameter_list|(
name|Message
name|message
parameter_list|,
name|HBaseData
name|data
parameter_list|)
block|{
if|if
condition|(
name|data
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|message
operator|.
name|setBody
argument_list|(
name|data
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

