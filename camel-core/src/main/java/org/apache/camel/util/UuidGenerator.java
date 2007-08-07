begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.util
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|InetAddress
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|ServerSocket
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
name|Level
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

begin_comment
comment|/**  * Generator for Globally unique Strings.  */
end_comment

begin_class
DECL|class|UuidGenerator
specifier|public
class|class
name|UuidGenerator
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|Logger
operator|.
name|getLogger
argument_list|(
name|UuidGenerator
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
DECL|field|UNIQUE_STUB
specifier|private
specifier|static
specifier|final
name|String
name|UNIQUE_STUB
decl_stmt|;
DECL|field|instanceCount
specifier|private
specifier|static
name|int
name|instanceCount
decl_stmt|;
DECL|field|hostName
specifier|private
specifier|static
name|String
name|hostName
decl_stmt|;
DECL|field|seed
specifier|private
name|String
name|seed
decl_stmt|;
DECL|field|sequence
specifier|private
name|long
name|sequence
decl_stmt|;
static|static
block|{
name|String
name|stub
init|=
literal|""
decl_stmt|;
name|boolean
name|canAccessSystemProps
init|=
literal|true
decl_stmt|;
try|try
block|{
name|SecurityManager
name|sm
init|=
name|System
operator|.
name|getSecurityManager
argument_list|()
decl_stmt|;
if|if
condition|(
name|sm
operator|!=
literal|null
condition|)
block|{
name|sm
operator|.
name|checkPropertiesAccess
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|SecurityException
name|se
parameter_list|)
block|{
name|canAccessSystemProps
operator|=
literal|false
expr_stmt|;
block|}
if|if
condition|(
name|canAccessSystemProps
condition|)
block|{
try|try
block|{
name|hostName
operator|=
name|InetAddress
operator|.
name|getLocalHost
argument_list|()
operator|.
name|getHostName
argument_list|()
expr_stmt|;
name|ServerSocket
name|ss
init|=
operator|new
name|ServerSocket
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|stub
operator|=
literal|"/"
operator|+
name|ss
operator|.
name|getLocalPort
argument_list|()
operator|+
literal|"-"
operator|+
name|System
operator|.
name|currentTimeMillis
argument_list|()
operator|+
literal|"/"
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|100
argument_list|)
expr_stmt|;
name|ss
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ioe
parameter_list|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|,
literal|"could not generate unique stub"
argument_list|,
name|ioe
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|hostName
operator|=
literal|"localhost"
expr_stmt|;
name|stub
operator|=
literal|"-1-"
operator|+
name|System
operator|.
name|currentTimeMillis
argument_list|()
operator|+
literal|"-"
expr_stmt|;
block|}
name|UNIQUE_STUB
operator|=
name|stub
expr_stmt|;
block|}
comment|/**      * Construct an IdGenerator      *       */
DECL|method|UuidGenerator (String prefix)
specifier|public
name|UuidGenerator
parameter_list|(
name|String
name|prefix
parameter_list|)
block|{
synchronized|synchronized
init|(
name|UNIQUE_STUB
init|)
block|{
name|this
operator|.
name|seed
operator|=
name|prefix
operator|+
name|UNIQUE_STUB
operator|+
operator|(
name|instanceCount
operator|++
operator|)
operator|+
literal|"-"
expr_stmt|;
block|}
block|}
DECL|method|UuidGenerator ()
specifier|public
name|UuidGenerator
parameter_list|()
block|{
name|this
argument_list|(
literal|"ID-"
operator|+
name|hostName
argument_list|)
expr_stmt|;
block|}
comment|/**      * As we have to find the hostname as a side-affect of generating a unique      * stub, we allow it's easy retrevial here      *       * @return the local host name      */
DECL|method|getHostName ()
specifier|public
specifier|static
name|String
name|getHostName
parameter_list|()
block|{
return|return
name|hostName
return|;
block|}
comment|/**      * Generate a unqiue id      *       * @return a unique id      */
DECL|method|generateId ()
specifier|public
specifier|synchronized
name|String
name|generateId
parameter_list|()
block|{
return|return
name|this
operator|.
name|seed
operator|+
operator|(
name|this
operator|.
name|sequence
operator|++
operator|)
return|;
block|}
comment|/**      * Generate a unique ID - that is friendly for a URL or file system      *       * @return a unique id      */
DECL|method|generateSanitizedId ()
specifier|public
name|String
name|generateSanitizedId
parameter_list|()
block|{
name|String
name|result
init|=
name|generateId
argument_list|()
decl_stmt|;
name|result
operator|=
name|result
operator|.
name|replace
argument_list|(
literal|':'
argument_list|,
literal|'-'
argument_list|)
expr_stmt|;
name|result
operator|=
name|result
operator|.
name|replace
argument_list|(
literal|'_'
argument_list|,
literal|'-'
argument_list|)
expr_stmt|;
name|result
operator|=
name|result
operator|.
name|replace
argument_list|(
literal|'.'
argument_list|,
literal|'-'
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
block|}
end_class

end_unit

