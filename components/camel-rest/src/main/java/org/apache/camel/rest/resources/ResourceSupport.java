begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.rest.resources
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|rest
operator|.
name|resources
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|GET
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|Produces
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|Path
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|PathParam
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|MediaType
import|;
end_import

begin_import
import|import
name|com
operator|.
name|sun
operator|.
name|jersey
operator|.
name|api
operator|.
name|view
operator|.
name|Viewable
import|;
end_import

begin_comment
comment|/**  * A base class for any resource which is viewable in HTML  *  * @version $Revision: 1.1 $  */
end_comment

begin_class
DECL|class|ResourceSupport
specifier|public
specifier|abstract
class|class
name|ResourceSupport
block|{
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"{view:\\w*}"
argument_list|)
annotation|@
name|Produces
argument_list|(
block|{
name|MediaType
operator|.
name|TEXT_HTML
block|}
argument_list|)
DECL|method|view (@athParamR) String view)
specifier|public
name|Viewable
name|view
parameter_list|(
annotation|@
name|PathParam
argument_list|(
literal|"view"
argument_list|)
name|String
name|view
parameter_list|)
block|{
if|if
condition|(
name|view
operator|==
literal|null
operator|||
name|view
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
name|view
operator|=
literal|"index"
expr_stmt|;
block|}
return|return
operator|new
name|Viewable
argument_list|(
name|view
argument_list|,
name|this
argument_list|)
return|;
block|}
comment|// TODO remove redunant non-DRY code ASAP
comment|//
comment|// The following redundant methods are here
comment|// until there is a way to specify a higher priority for HTML views
comment|//
comment|// for more details see these issues
comment|//
comment|// https://jsr311.dev.java.net/issues/show_bug.cgi?id=65
comment|// https://jsr311.dev.java.net/issues/show_bug.cgi?id=46
annotation|@
name|GET
annotation|@
name|Produces
argument_list|(
block|{
name|MediaType
operator|.
name|TEXT_HTML
block|}
argument_list|)
DECL|method|index ()
specifier|public
name|Viewable
name|index
parameter_list|()
block|{
return|return
name|view
argument_list|(
literal|"index"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

