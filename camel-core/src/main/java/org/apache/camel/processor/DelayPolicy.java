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
name|java
operator|.
name|io
operator|.
name|Serializable
import|;
end_import

begin_comment
comment|/**  * The base policy used when a fixed delay is needed.  *<p/>  * This policy is used by  *<a href="http://activemq.apache.org/camel/transactional-client.html">Transactional client</a>  * and<a href="http://activemq.apache.org/camel/dead-letter-channel.html">Dead Letter Channel</a>.  *  * The default values is:  *<ul>  *<li>delay = 1000L</li>  *</ul>  *<p/>  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|DelayPolicy
specifier|public
class|class
name|DelayPolicy
implements|implements
name|Cloneable
implements|,
name|Serializable
block|{
DECL|field|delay
specifier|protected
name|long
name|delay
init|=
literal|1000L
decl_stmt|;
DECL|method|DelayPolicy ()
specifier|public
name|DelayPolicy
parameter_list|()
block|{     }
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"DelayPolicy[delay="
operator|+
name|delay
operator|+
literal|"]"
return|;
block|}
DECL|method|copy ()
specifier|public
name|DelayPolicy
name|copy
parameter_list|()
block|{
try|try
block|{
return|return
operator|(
name|DelayPolicy
operator|)
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
operator|new
name|RuntimeException
argument_list|(
literal|"Could not clone: "
operator|+
name|e
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
comment|// Builder methods
comment|// -------------------------------------------------------------------------
comment|/**      * Sets the delay in milliseconds      */
DECL|method|delay (long delay)
specifier|public
name|DelayPolicy
name|delay
parameter_list|(
name|long
name|delay
parameter_list|)
block|{
name|setDelay
argument_list|(
name|delay
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|// Properties
comment|// -------------------------------------------------------------------------
DECL|method|getDelay ()
specifier|public
name|long
name|getDelay
parameter_list|()
block|{
return|return
name|delay
return|;
block|}
comment|/**      * Sets the delay in milliseconds      */
DECL|method|setDelay (long delay)
specifier|public
name|void
name|setDelay
parameter_list|(
name|long
name|delay
parameter_list|)
block|{
name|this
operator|.
name|delay
operator|=
name|delay
expr_stmt|;
block|}
block|}
end_class

end_unit

