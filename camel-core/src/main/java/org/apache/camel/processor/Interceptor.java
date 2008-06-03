begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
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
name|Intercept
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
name|Processor
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
name|ServiceHelper
import|;
end_import

begin_comment
comment|/**  * An interceptor which provides the processing logic as a pluggable processor  * which allows the {@link #proceed(Exchange)} method to be called at some point  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|Interceptor
specifier|public
class|class
name|Interceptor
extends|extends
name|DelegateProcessor
implements|implements
name|Intercept
block|{
DECL|field|interceptorLogic
specifier|private
name|Processor
name|interceptorLogic
decl_stmt|;
DECL|method|Interceptor ()
specifier|public
name|Interceptor
parameter_list|()
block|{     }
DECL|method|Interceptor (Processor interceptorLogic)
specifier|public
name|Interceptor
parameter_list|(
name|Processor
name|interceptorLogic
parameter_list|)
block|{
name|this
operator|.
name|interceptorLogic
operator|=
name|interceptorLogic
expr_stmt|;
block|}
DECL|method|process (Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|interceptorLogic
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
DECL|method|getInterceptorLogic ()
specifier|public
name|Processor
name|getInterceptorLogic
parameter_list|()
block|{
return|return
name|interceptorLogic
return|;
block|}
DECL|method|setInterceptorLogic (Processor interceptorLogic)
specifier|public
name|void
name|setInterceptorLogic
parameter_list|(
name|Processor
name|interceptorLogic
parameter_list|)
block|{
name|this
operator|.
name|interceptorLogic
operator|=
name|interceptorLogic
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
name|ServiceHelper
operator|.
name|startService
argument_list|(
name|interceptorLogic
argument_list|)
expr_stmt|;
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
name|ServiceHelper
operator|.
name|stopService
argument_list|(
name|interceptorLogic
argument_list|)
expr_stmt|;
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

