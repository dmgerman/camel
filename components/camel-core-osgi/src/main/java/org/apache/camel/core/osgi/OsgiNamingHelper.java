begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.core.osgi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|core
operator|.
name|osgi
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|atomic
operator|.
name|AtomicInteger
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
name|osgi
operator|.
name|framework
operator|.
name|BundleContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|InvalidSyntaxException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|ServiceReference
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_comment
comment|/**  * A helper to find free names in the OSGi service registry.  */
end_comment

begin_class
DECL|class|OsgiNamingHelper
specifier|public
specifier|final
class|class
name|OsgiNamingHelper
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|OsgiNamingHelper
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|OsgiNamingHelper ()
specifier|private
name|OsgiNamingHelper
parameter_list|()
block|{     }
comment|/**      * Checks the OSGi service registry for a free name (uses the counter if there is a clash to find next free name)      *      * @param context the bundle context      * @param prefix  the prefix for the name      * @param key     the key to use in the OSGi filter; either {@link OsgiCamelContextPublisher#CONTEXT_NAME_PROPERTY}      *                or {@link OsgiCamelContextPublisher#CONTEXT_MANAGEMENT_NAME_PROPERTY}.      * @param counter the counter      * @param checkFirst<tt>true</tt> to check the prefix name as-is before using the counter,<tt>false</tt> the counter is used immediately      * @return the free name, is never<tt>null</tt>      */
DECL|method|findFreeCamelContextName (BundleContext context, String prefix, String key, AtomicInteger counter, boolean checkFirst)
specifier|public
specifier|static
name|String
name|findFreeCamelContextName
parameter_list|(
name|BundleContext
name|context
parameter_list|,
name|String
name|prefix
parameter_list|,
name|String
name|key
parameter_list|,
name|AtomicInteger
name|counter
parameter_list|,
name|boolean
name|checkFirst
parameter_list|)
block|{
name|String
name|candidate
init|=
literal|null
decl_stmt|;
name|boolean
name|clash
init|=
literal|false
decl_stmt|;
do|do
block|{
try|try
block|{
name|clash
operator|=
literal|false
expr_stmt|;
if|if
condition|(
name|candidate
operator|==
literal|null
operator|&&
name|checkFirst
condition|)
block|{
comment|// try candidate as-is
name|candidate
operator|=
name|prefix
expr_stmt|;
block|}
else|else
block|{
comment|// generate new candidate
name|candidate
operator|=
name|prefix
operator|+
literal|"-"
operator|+
name|getNextCounter
argument_list|(
name|counter
argument_list|)
expr_stmt|;
block|}
name|LOG
operator|.
name|trace
argument_list|(
literal|"Checking OSGi Service Registry for existence of existing CamelContext with name: {}"
argument_list|,
name|candidate
argument_list|)
expr_stmt|;
name|ServiceReference
argument_list|<
name|?
argument_list|>
index|[]
name|refs
init|=
name|context
operator|.
name|getServiceReferences
argument_list|(
name|CamelContext
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
literal|"("
operator|+
name|key
operator|+
literal|"="
operator|+
name|candidate
operator|+
literal|")"
argument_list|)
decl_stmt|;
if|if
condition|(
name|refs
operator|!=
literal|null
operator|&&
name|refs
operator|.
name|length
operator|>
literal|0
condition|)
block|{
for|for
control|(
name|ServiceReference
argument_list|<
name|?
argument_list|>
name|ref
range|:
name|refs
control|)
block|{
name|Object
name|id
init|=
name|ref
operator|.
name|getProperty
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|id
operator|!=
literal|null
operator|&&
name|candidate
operator|.
name|equals
argument_list|(
name|id
argument_list|)
condition|)
block|{
name|clash
operator|=
literal|true
expr_stmt|;
break|break;
block|}
block|}
block|}
block|}
catch|catch
parameter_list|(
name|InvalidSyntaxException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Error finding free Camel name in OSGi Service Registry due "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
operator|+
literal|". This exception is ignored."
argument_list|,
name|e
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
do|while
condition|(
name|clash
condition|)
do|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Generated free name for bundle id: {}, clash: {} -> {}"
argument_list|,
name|context
operator|.
name|getBundle
argument_list|()
operator|.
name|getBundleId
argument_list|()
argument_list|,
name|clash
argument_list|,
name|candidate
argument_list|)
expr_stmt|;
return|return
name|candidate
return|;
block|}
DECL|method|getNextCounter (AtomicInteger counter)
specifier|public
specifier|static
name|int
name|getNextCounter
parameter_list|(
name|AtomicInteger
name|counter
parameter_list|)
block|{
comment|// we want to start counting from 1, so increment first
return|return
name|counter
operator|.
name|incrementAndGet
argument_list|()
return|;
block|}
block|}
end_class

end_unit

