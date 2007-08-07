begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.direct
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|direct
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
import|;
end_import

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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ScheduledExecutorService
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
name|Component
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
name|Exchange
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
name|util
operator|.
name|IntrospectionSupport
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
name|util
operator|.
name|ObjectHelper
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
name|util
operator|.
name|URISupport
import|;
end_import

begin_comment
comment|/**  * Represents the component that manages {@link DirectEndpoint}. It holds the  * list of named direct endpoints.  *   * @org.apache.xbean.XBean  * @version $Revision: 519973 $  */
end_comment

begin_class
DECL|class|DirectComponent
specifier|public
class|class
name|DirectComponent
parameter_list|<
name|E
extends|extends
name|Exchange
parameter_list|>
implements|implements
name|Component
argument_list|<
name|E
argument_list|>
block|{
DECL|field|context
specifier|private
name|CamelContext
name|context
decl_stmt|;
DECL|method|getCamelContext ()
specifier|public
name|CamelContext
name|getCamelContext
parameter_list|()
block|{
return|return
name|context
return|;
block|}
DECL|method|getExecutorService ()
specifier|public
name|ScheduledExecutorService
name|getExecutorService
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
DECL|method|createEndpoint (String uri)
specifier|public
name|Endpoint
argument_list|<
name|E
argument_list|>
name|createEndpoint
parameter_list|(
name|String
name|uri
parameter_list|)
throws|throws
name|Exception
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
literal|"camelContext"
argument_list|)
expr_stmt|;
name|URI
name|u
init|=
operator|new
name|URI
argument_list|(
name|uri
argument_list|)
decl_stmt|;
name|Map
name|parameters
init|=
name|URISupport
operator|.
name|parseParamters
argument_list|(
name|u
argument_list|)
decl_stmt|;
name|Endpoint
argument_list|<
name|E
argument_list|>
name|endpoint
init|=
operator|new
name|DirectEndpoint
argument_list|<
name|E
argument_list|>
argument_list|(
name|uri
argument_list|,
name|this
argument_list|)
decl_stmt|;
if|if
condition|(
name|parameters
operator|!=
literal|null
condition|)
block|{
name|IntrospectionSupport
operator|.
name|setProperties
argument_list|(
name|endpoint
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
block|}
return|return
name|endpoint
return|;
block|}
DECL|method|setCamelContext (CamelContext context)
specifier|public
name|void
name|setCamelContext
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
name|this
operator|.
name|context
operator|=
name|context
expr_stmt|;
block|}
block|}
end_class

end_unit

