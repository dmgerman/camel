begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring.boot
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|boot
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Properties
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
name|StringHelper
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

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|ApplicationContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|core
operator|.
name|env
operator|.
name|PropertySource
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|core
operator|.
name|io
operator|.
name|Resource
import|;
end_import

begin_comment
comment|/**  * To load properties from files, such as a secret mounted to the container.  */
end_comment

begin_class
DECL|class|FilePropertySource
specifier|public
class|class
name|FilePropertySource
extends|extends
name|PropertySource
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
name|FilePropertySource
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// properties for all the loaded files
DECL|field|properties
specifier|private
specifier|final
name|Properties
name|properties
decl_stmt|;
DECL|method|FilePropertySource (String name, ApplicationContext applicationContext, String directory)
specifier|public
name|FilePropertySource
parameter_list|(
name|String
name|name
parameter_list|,
name|ApplicationContext
name|applicationContext
parameter_list|,
name|String
name|directory
parameter_list|)
block|{
name|super
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|StringHelper
operator|.
name|notEmpty
argument_list|(
name|directory
argument_list|,
literal|"directory"
argument_list|)
expr_stmt|;
name|Properties
name|loaded
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
try|try
block|{
name|Resource
index|[]
name|files
init|=
name|applicationContext
operator|.
name|getResources
argument_list|(
name|directory
argument_list|)
decl_stmt|;
for|for
control|(
name|Resource
name|file
range|:
name|files
control|)
block|{
if|if
condition|(
name|file
operator|.
name|exists
argument_list|()
condition|)
block|{
try|try
init|(
name|FileInputStream
name|fis
init|=
operator|new
name|FileInputStream
argument_list|(
name|file
operator|.
name|getFile
argument_list|()
argument_list|)
init|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Loading properties from file: {}"
argument_list|,
name|file
argument_list|)
expr_stmt|;
name|Properties
name|extra
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|extra
operator|.
name|load
argument_list|(
name|fis
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|extra
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|loaded
operator|.
name|putAll
argument_list|(
name|extra
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|// ignore
block|}
block|}
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|// ignore
block|}
comment|// if we loaded any files then store as properties
if|if
condition|(
name|loaded
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|properties
operator|=
literal|null
expr_stmt|;
name|LOG
operator|.
name|warn
argument_list|(
literal|"No properties found while loading from: {}"
argument_list|,
name|directory
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|properties
operator|=
name|loaded
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Loaded {} properties from: {}"
argument_list|,
name|properties
operator|.
name|size
argument_list|()
argument_list|,
name|directory
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|getProperty (String name)
specifier|public
name|Object
name|getProperty
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|Object
name|answer
init|=
name|properties
operator|!=
literal|null
condition|?
name|properties
operator|.
name|getProperty
argument_list|(
name|name
argument_list|)
else|:
literal|null
decl_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"getProperty {} -> {}"
argument_list|,
name|name
argument_list|,
name|answer
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
block|}
end_class

end_unit

