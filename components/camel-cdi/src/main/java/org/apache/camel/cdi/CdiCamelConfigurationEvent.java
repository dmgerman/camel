begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.cdi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|cdi
package|;
end_package

begin_comment
DECL|class|CdiCamelConfigurationEvent
comment|/* package-private */
end_comment

begin_class
DECL|class|CdiCamelConfigurationEvent
specifier|final
class|class
name|CdiCamelConfigurationEvent
implements|implements
name|CdiCamelConfiguration
block|{
DECL|field|autoConfigureRoutes
specifier|private
name|boolean
name|autoConfigureRoutes
init|=
literal|true
decl_stmt|;
DECL|field|autoStartContexts
specifier|private
name|boolean
name|autoStartContexts
init|=
literal|true
decl_stmt|;
DECL|field|unmodifiable
specifier|private
specifier|volatile
name|boolean
name|unmodifiable
decl_stmt|;
annotation|@
name|Override
DECL|method|autoConfigureRoutes (boolean autoConfigureRoutes)
specifier|public
name|CdiCamelConfiguration
name|autoConfigureRoutes
parameter_list|(
name|boolean
name|autoConfigureRoutes
parameter_list|)
block|{
name|throwsIfUnmodifiable
argument_list|()
expr_stmt|;
name|this
operator|.
name|autoConfigureRoutes
operator|=
name|autoConfigureRoutes
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
DECL|method|autoConfigureRoutes ()
specifier|public
name|boolean
name|autoConfigureRoutes
parameter_list|()
block|{
return|return
name|autoConfigureRoutes
return|;
block|}
annotation|@
name|Override
DECL|method|autoStartContexts (boolean autoStartContexts)
specifier|public
name|CdiCamelConfiguration
name|autoStartContexts
parameter_list|(
name|boolean
name|autoStartContexts
parameter_list|)
block|{
name|throwsIfUnmodifiable
argument_list|()
expr_stmt|;
name|this
operator|.
name|autoStartContexts
operator|=
name|autoStartContexts
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
DECL|method|autoStartContexts ()
specifier|public
name|boolean
name|autoStartContexts
parameter_list|()
block|{
return|return
name|autoStartContexts
return|;
block|}
DECL|method|unmodifiable ()
name|void
name|unmodifiable
parameter_list|()
block|{
name|unmodifiable
operator|=
literal|true
expr_stmt|;
block|}
DECL|method|throwsIfUnmodifiable ()
specifier|private
name|void
name|throwsIfUnmodifiable
parameter_list|()
block|{
if|if
condition|(
name|unmodifiable
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Camel CDI configuration event must not be used outside "
operator|+
literal|"its observer method!"
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

