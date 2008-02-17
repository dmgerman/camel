begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf.feature
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
name|feature
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Logger
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
name|cxf
operator|.
name|interceptors
operator|.
name|DOMInInterceptor
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
name|cxf
operator|.
name|interceptors
operator|.
name|DOMOutInterceptor
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
name|cxf
operator|.
name|interceptors
operator|.
name|PayloadContentRedirectInterceptor
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
name|Bus
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
name|binding
operator|.
name|Binding
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
name|common
operator|.
name|logging
operator|.
name|LogUtils
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
name|endpoint
operator|.
name|Server
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
name|phase
operator|.
name|Phase
import|;
end_import

begin_comment
comment|/**  * This feature just setting up the CXF endpoint interceptor for handling the  * Message in PAYLOAD data format  */
end_comment

begin_class
DECL|class|PayLoadDataFormatFeature
specifier|public
class|class
name|PayLoadDataFormatFeature
extends|extends
name|AbstractDataFormatFeature
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|MessageDataFormatFeature
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// filiter the unused phase
DECL|field|REMOVING_IN_PHASES
specifier|private
specifier|static
specifier|final
name|String
index|[]
name|REMOVING_IN_PHASES
init|=
block|{
name|Phase
operator|.
name|UNMARSHAL
block|}
decl_stmt|;
DECL|field|REMOVING_OUT_PHASES
specifier|private
specifier|static
specifier|final
name|String
index|[]
name|REMOVING_OUT_PHASES
init|=
block|{
name|Phase
operator|.
name|MARSHAL
block|,
name|Phase
operator|.
name|MARSHAL_ENDING
block|}
decl_stmt|;
annotation|@
name|Override
DECL|method|initialize (Client client, Bus bus)
specifier|public
name|void
name|initialize
parameter_list|(
name|Client
name|client
parameter_list|,
name|Bus
name|bus
parameter_list|)
block|{
name|removeInterceptorWhichIsInThePhases
argument_list|(
name|client
operator|.
name|getInInterceptors
argument_list|()
argument_list|,
name|REMOVING_IN_PHASES
argument_list|)
expr_stmt|;
name|removeInterceptorWhichIsInThePhases
argument_list|(
name|client
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getService
argument_list|()
operator|.
name|getInInterceptors
argument_list|()
argument_list|,
name|REMOVING_IN_PHASES
argument_list|)
expr_stmt|;
name|removeInterceptorWhichIsInThePhases
argument_list|(
name|client
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getInInterceptors
argument_list|()
argument_list|,
name|REMOVING_IN_PHASES
argument_list|)
expr_stmt|;
name|removeInterceptorWhichIsInThePhases
argument_list|(
name|client
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getBinding
argument_list|()
operator|.
name|getInInterceptors
argument_list|()
argument_list|,
name|REMOVING_IN_PHASES
argument_list|)
expr_stmt|;
name|removeInterceptorWhichIsInThePhases
argument_list|(
name|client
operator|.
name|getOutInterceptors
argument_list|()
argument_list|,
name|REMOVING_OUT_PHASES
argument_list|)
expr_stmt|;
name|removeInterceptorWhichIsInThePhases
argument_list|(
name|client
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getService
argument_list|()
operator|.
name|getOutInterceptors
argument_list|()
argument_list|,
name|REMOVING_OUT_PHASES
argument_list|)
expr_stmt|;
name|removeInterceptorWhichIsInThePhases
argument_list|(
name|client
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getOutInterceptors
argument_list|()
argument_list|,
name|REMOVING_OUT_PHASES
argument_list|)
expr_stmt|;
name|removeInterceptorWhichIsInThePhases
argument_list|(
name|client
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getBinding
argument_list|()
operator|.
name|getOutInterceptors
argument_list|()
argument_list|,
name|REMOVING_OUT_PHASES
argument_list|)
expr_stmt|;
name|addDataHandlingInterceptors
argument_list|(
name|client
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getBinding
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|initialize (Server server, Bus bus)
specifier|public
name|void
name|initialize
parameter_list|(
name|Server
name|server
parameter_list|,
name|Bus
name|bus
parameter_list|)
block|{
name|removeInterceptorWhichIsInThePhases
argument_list|(
name|server
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getService
argument_list|()
operator|.
name|getInInterceptors
argument_list|()
argument_list|,
name|REMOVING_IN_PHASES
argument_list|)
expr_stmt|;
name|removeInterceptorWhichIsInThePhases
argument_list|(
name|server
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getInInterceptors
argument_list|()
argument_list|,
name|REMOVING_IN_PHASES
argument_list|)
expr_stmt|;
name|removeInterceptorWhichIsInThePhases
argument_list|(
name|server
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getBinding
argument_list|()
operator|.
name|getInInterceptors
argument_list|()
argument_list|,
name|REMOVING_IN_PHASES
argument_list|)
expr_stmt|;
name|removeInterceptorWhichIsInThePhases
argument_list|(
name|server
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getService
argument_list|()
operator|.
name|getOutInterceptors
argument_list|()
argument_list|,
name|REMOVING_OUT_PHASES
argument_list|)
expr_stmt|;
name|removeInterceptorWhichIsInThePhases
argument_list|(
name|server
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getOutInterceptors
argument_list|()
argument_list|,
name|REMOVING_OUT_PHASES
argument_list|)
expr_stmt|;
name|removeInterceptorWhichIsInThePhases
argument_list|(
name|server
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getBinding
argument_list|()
operator|.
name|getOutInterceptors
argument_list|()
argument_list|,
name|REMOVING_OUT_PHASES
argument_list|)
expr_stmt|;
comment|// set the invoker interceptor
name|resetServiceInvokerInterceptor
argument_list|(
name|server
argument_list|)
expr_stmt|;
name|addDataHandlingInterceptors
argument_list|(
name|server
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getBinding
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|addDataHandlingInterceptors (Binding binding)
specifier|private
name|void
name|addDataHandlingInterceptors
parameter_list|(
name|Binding
name|binding
parameter_list|)
block|{
name|binding
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|DOMInInterceptor
argument_list|()
argument_list|)
expr_stmt|;
name|binding
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|DOMOutInterceptor
argument_list|()
argument_list|)
expr_stmt|;
name|binding
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|PayloadContentRedirectInterceptor
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getLogger ()
specifier|protected
name|Logger
name|getLogger
parameter_list|()
block|{
return|return
name|LOG
return|;
block|}
block|}
end_class

end_unit

