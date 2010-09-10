begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Collection
import|;
end_import

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
name|cxf
operator|.
name|feature
operator|.
name|AbstractFeature
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
name|interceptor
operator|.
name|Interceptor
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
name|PhaseInterceptor
import|;
end_import

begin_comment
comment|/**  * The abstract class for the data format feature  */
end_comment

begin_class
DECL|class|AbstractDataFormatFeature
specifier|public
specifier|abstract
class|class
name|AbstractDataFormatFeature
extends|extends
name|AbstractFeature
block|{
DECL|method|getLogger ()
specifier|protected
specifier|abstract
name|Logger
name|getLogger
parameter_list|()
function_decl|;
DECL|method|removeInterceptorWhichIsInThePhases (List<Interceptor<? extends Message>> interceptors, String[] phaseNames)
specifier|protected
name|void
name|removeInterceptorWhichIsInThePhases
parameter_list|(
name|List
argument_list|<
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
argument_list|>
name|interceptors
parameter_list|,
name|String
index|[]
name|phaseNames
parameter_list|)
block|{
for|for
control|(
name|Interceptor
name|i
range|:
name|interceptors
control|)
block|{
if|if
condition|(
name|i
operator|instanceof
name|PhaseInterceptor
condition|)
block|{
name|PhaseInterceptor
name|p
init|=
operator|(
name|PhaseInterceptor
operator|)
name|i
decl_stmt|;
for|for
control|(
name|String
name|phaseName
range|:
name|phaseNames
control|)
block|{
if|if
condition|(
name|p
operator|.
name|getPhase
argument_list|()
operator|.
name|equals
argument_list|(
name|phaseName
argument_list|)
condition|)
block|{
name|getLogger
argument_list|()
operator|.
name|info
argument_list|(
literal|"removing the interceptor "
operator|+
name|p
argument_list|)
expr_stmt|;
name|interceptors
operator|.
name|remove
argument_list|(
name|p
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
block|}
block|}
block|}
DECL|method|removeInterceptorWhichIsOutThePhases (List<Interceptor<? extends Message>> interceptors, String[] phaseNames)
specifier|protected
name|void
name|removeInterceptorWhichIsOutThePhases
parameter_list|(
name|List
argument_list|<
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
argument_list|>
name|interceptors
parameter_list|,
name|String
index|[]
name|phaseNames
parameter_list|)
block|{
for|for
control|(
name|Interceptor
name|i
range|:
name|interceptors
control|)
block|{
name|boolean
name|outside
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|i
operator|instanceof
name|PhaseInterceptor
condition|)
block|{
name|PhaseInterceptor
name|p
init|=
operator|(
name|PhaseInterceptor
operator|)
name|i
decl_stmt|;
for|for
control|(
name|String
name|phaseName
range|:
name|phaseNames
control|)
block|{
if|if
condition|(
name|p
operator|.
name|getPhase
argument_list|()
operator|.
name|equals
argument_list|(
name|phaseName
argument_list|)
condition|)
block|{
name|outside
operator|=
literal|true
expr_stmt|;
break|break;
block|}
block|}
if|if
condition|(
operator|!
name|outside
condition|)
block|{
name|getLogger
argument_list|()
operator|.
name|info
argument_list|(
literal|"removing the interceptor "
operator|+
name|p
argument_list|)
expr_stmt|;
name|interceptors
operator|.
name|remove
argument_list|(
name|p
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
DECL|method|removeInterceptors (List<Interceptor<? extends Message>> interceptors, Collection<Class> toBeRemovedInterceptors)
specifier|protected
name|void
name|removeInterceptors
parameter_list|(
name|List
argument_list|<
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
argument_list|>
name|interceptors
parameter_list|,
name|Collection
argument_list|<
name|Class
argument_list|>
name|toBeRemovedInterceptors
parameter_list|)
block|{
for|for
control|(
name|Interceptor
name|interceptor
range|:
name|interceptors
control|)
block|{
if|if
condition|(
name|toBeRemovedInterceptors
operator|.
name|contains
argument_list|(
name|interceptor
operator|.
name|getClass
argument_list|()
argument_list|)
condition|)
block|{
name|getLogger
argument_list|()
operator|.
name|info
argument_list|(
literal|"removing the interceptor "
operator|+
name|interceptor
argument_list|)
expr_stmt|;
name|interceptors
operator|.
name|remove
argument_list|(
name|interceptor
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

