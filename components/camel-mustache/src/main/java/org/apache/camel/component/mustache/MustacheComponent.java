begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.mustache
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|mustache
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|com
operator|.
name|github
operator|.
name|mustachejava
operator|.
name|DefaultMustacheFactory
import|;
end_import

begin_import
import|import
name|com
operator|.
name|github
operator|.
name|mustachejava
operator|.
name|MustacheFactory
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
name|Endpoint
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
name|impl
operator|.
name|DefaultComponent
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
name|Metadata
import|;
end_import

begin_comment
comment|/**  * Represents the component that manages {@link MustacheEndpoint}.  * URI pattern: {@code mustache://template_name.mustache}  * Supports parameters:  *<ul>  *<li>encoding: default platform one</li>  *<li>startDelimiter: default "{{"</li>  *<li>endDelimiter: default "}}"</li>  *</li>  */
end_comment

begin_class
DECL|class|MustacheComponent
specifier|public
class|class
name|MustacheComponent
extends|extends
name|DefaultComponent
block|{
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|mustacheFactory
specifier|private
name|MustacheFactory
name|mustacheFactory
init|=
operator|new
name|DefaultMustacheFactory
argument_list|()
decl_stmt|;
DECL|method|MustacheComponent ()
specifier|public
name|MustacheComponent
parameter_list|()
block|{     }
annotation|@
name|Override
DECL|method|createEndpoint (String uri, String remaining, Map<String, Object> parameters)
specifier|protected
name|Endpoint
name|createEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|remaining
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
throws|throws
name|Exception
block|{
name|MustacheEndpoint
name|endpoint
init|=
operator|new
name|MustacheEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|,
name|remaining
argument_list|)
decl_stmt|;
name|endpoint
operator|.
name|setMustacheFactory
argument_list|(
name|getMustacheFactory
argument_list|()
argument_list|)
expr_stmt|;
name|setProperties
argument_list|(
name|endpoint
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
return|return
name|endpoint
return|;
block|}
DECL|method|getMustacheFactory ()
specifier|public
name|MustacheFactory
name|getMustacheFactory
parameter_list|()
block|{
return|return
name|mustacheFactory
return|;
block|}
comment|/**      * To use a custom {@link MustacheFactory}      */
DECL|method|setMustacheFactory (MustacheFactory mustacheFactory)
specifier|public
name|void
name|setMustacheFactory
parameter_list|(
name|MustacheFactory
name|mustacheFactory
parameter_list|)
block|{
name|this
operator|.
name|mustacheFactory
operator|=
name|mustacheFactory
expr_stmt|;
block|}
block|}
end_class

end_unit

