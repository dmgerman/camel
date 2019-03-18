begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf.common.header
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
operator|.
name|common
operator|.
name|header
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spi
operator|.
name|HeaderFilterStrategy
operator|.
name|Direction
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
name|headers
operator|.
name|Header
import|;
end_import

begin_comment
comment|/**  * Filter the wire message headers from one CXF endpoint to another CXF endpoint  */
end_comment

begin_interface
DECL|interface|MessageHeaderFilter
specifier|public
interface|interface
name|MessageHeaderFilter
block|{
comment|/**      * @return a list of binding name spaces that this relay can service      */
DECL|method|getActivationNamespaces ()
name|List
argument_list|<
name|String
argument_list|>
name|getActivationNamespaces
parameter_list|()
function_decl|;
comment|/**      *  This method filters (removes) headers from the given header list.       *<i>Out</i> direction refers to processing headers from a Camel message to an       *  CXF message.<i>In</i> direction is the reverse direction.      *        *  @param direction the direction of the processing      *  @param headers the origin list of headers      */
DECL|method|filter (Direction direction, List<Header> headers)
name|void
name|filter
parameter_list|(
name|Direction
name|direction
parameter_list|,
name|List
argument_list|<
name|Header
argument_list|>
name|headers
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

