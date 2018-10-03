begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl.cluster
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
operator|.
name|cluster
package|;
end_package

begin_import
import|import
name|java
operator|.
name|time
operator|.
name|Duration
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
name|RuntimeCamelException
import|;
end_import

begin_class
DECL|class|ClusteredRouteConfiguration
specifier|public
class|class
name|ClusteredRouteConfiguration
implements|implements
name|Cloneable
block|{
DECL|field|namespace
specifier|private
name|String
name|namespace
decl_stmt|;
DECL|field|initialDelay
specifier|private
name|Duration
name|initialDelay
decl_stmt|;
DECL|method|getNamespace ()
specifier|public
name|String
name|getNamespace
parameter_list|()
block|{
return|return
name|namespace
return|;
block|}
DECL|method|setNamespace (String namespace)
specifier|public
name|void
name|setNamespace
parameter_list|(
name|String
name|namespace
parameter_list|)
block|{
name|this
operator|.
name|namespace
operator|=
name|namespace
expr_stmt|;
block|}
DECL|method|getInitialDelay ()
specifier|public
name|Duration
name|getInitialDelay
parameter_list|()
block|{
return|return
name|initialDelay
return|;
block|}
DECL|method|setInitialDelay (Duration initialDelay)
specifier|public
name|void
name|setInitialDelay
parameter_list|(
name|Duration
name|initialDelay
parameter_list|)
block|{
name|this
operator|.
name|initialDelay
operator|=
name|initialDelay
expr_stmt|;
block|}
comment|// ****************************************
comment|// Copy
comment|// ****************************************
DECL|method|copy ()
specifier|public
name|ClusteredRouteConfiguration
name|copy
parameter_list|()
block|{
try|try
block|{
return|return
operator|(
name|ClusteredRouteConfiguration
operator|)
name|super
operator|.
name|clone
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|CloneNotSupportedException
name|e
parameter_list|)
block|{
throw|throw
name|RuntimeCamelException
operator|.
name|wrapRuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

