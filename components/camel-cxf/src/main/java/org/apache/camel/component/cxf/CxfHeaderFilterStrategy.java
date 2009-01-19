begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|cxf
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
name|impl
operator|.
name|DefaultHeaderFilterStrategy
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|endpoint
operator|.
name|Client
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|Message
import|;
end_import

begin_comment
comment|/**  * The default CXF header filter strategy.  *   * @version $Revision$  */
end_comment

begin_class
DECL|class|CxfHeaderFilterStrategy
specifier|public
class|class
name|CxfHeaderFilterStrategy
extends|extends
name|DefaultHeaderFilterStrategy
block|{
DECL|method|CxfHeaderFilterStrategy ()
specifier|public
name|CxfHeaderFilterStrategy
parameter_list|()
block|{
name|initialize
argument_list|()
expr_stmt|;
block|}
DECL|method|initialize ()
specifier|protected
name|void
name|initialize
parameter_list|()
block|{
name|getOutFilter
argument_list|()
operator|.
name|add
argument_list|(
name|CxfConstants
operator|.
name|OPERATION_NAME
argument_list|)
expr_stmt|;
name|getOutFilter
argument_list|()
operator|.
name|add
argument_list|(
name|CxfConstants
operator|.
name|OPERATION_NAMESPACE
argument_list|)
expr_stmt|;
comment|// Request and response context Maps will be passed to CXF Client APIs
name|getOutFilter
argument_list|()
operator|.
name|add
argument_list|(
name|Client
operator|.
name|REQUEST_CONTEXT
argument_list|)
expr_stmt|;
name|getOutFilter
argument_list|()
operator|.
name|add
argument_list|(
name|Client
operator|.
name|RESPONSE_CONTEXT
argument_list|)
expr_stmt|;
comment|// protocol headers are stored as a Map.  DefaultCxfBinding
comment|// read the Map and send each entry to the filter.  Therefore,
comment|// we need to filter the header of this name.
name|getOutFilter
argument_list|()
operator|.
name|add
argument_list|(
name|Message
operator|.
name|PROTOCOL_HEADERS
argument_list|)
expr_stmt|;
name|getInFilter
argument_list|()
operator|.
name|add
argument_list|(
name|Message
operator|.
name|PROTOCOL_HEADERS
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

