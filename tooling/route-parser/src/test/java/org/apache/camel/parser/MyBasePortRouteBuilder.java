begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  Copyright 2005-2015 Red Hat, Inc.  *  *  Red Hat licenses this file to you under the Apache License, version  *  2.0 (the "License"); you may not use this file except in compliance  *  with the License.  You may obtain a copy of the License at  *  *     http://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing, software  *  distributed under the License is distributed on an "AS IS" BASIS,  *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or  *  implied.  See the License for the specific language governing  *  permissions and limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.parser
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|parser
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
name|builder
operator|.
name|RouteBuilder
import|;
end_import

begin_class
DECL|class|MyBasePortRouteBuilder
specifier|public
specifier|abstract
class|class
name|MyBasePortRouteBuilder
extends|extends
name|RouteBuilder
block|{
DECL|method|getNextPort ()
specifier|public
name|int
name|getNextPort
parameter_list|()
block|{
return|return
literal|8080
return|;
block|}
block|}
end_class

end_unit

