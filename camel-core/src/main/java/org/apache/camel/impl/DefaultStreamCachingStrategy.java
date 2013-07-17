begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
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
name|CamelContextAware
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
name|StreamCache
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
name|spi
operator|.
name|StreamCachingStrategy
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
name|FileUtil
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
name|IOHelper
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
comment|/**  * Default implementation of {@link StreamCachingStrategy}  */
end_comment

begin_class
DECL|class|DefaultStreamCachingStrategy
specifier|public
class|class
name|DefaultStreamCachingStrategy
extends|extends
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|support
operator|.
name|ServiceSupport
implements|implements
name|CamelContextAware
implements|,
name|StreamCachingStrategy
block|{
comment|// TODO: Add JMX management
comment|// TODO: Maybe use #syntax# for default temp dir so ppl can easily configure this
annotation|@
name|Deprecated
DECL|field|THRESHOLD
specifier|public
specifier|static
specifier|final
name|String
name|THRESHOLD
init|=
literal|"CamelCachedOutputStreamThreshold"
decl_stmt|;
annotation|@
name|Deprecated
DECL|field|BUFFER_SIZE
specifier|public
specifier|static
specifier|final
name|String
name|BUFFER_SIZE
init|=
literal|"CamelCachedOutputStreamBufferSize"
decl_stmt|;
annotation|@
name|Deprecated
DECL|field|TEMP_DIR
specifier|public
specifier|static
specifier|final
name|String
name|TEMP_DIR
init|=
literal|"CamelCachedOutputStreamOutputDirectory"
decl_stmt|;
annotation|@
name|Deprecated
DECL|field|CIPHER_TRANSFORMATION
specifier|public
specifier|static
specifier|final
name|String
name|CIPHER_TRANSFORMATION
init|=
literal|"CamelCachedOutputStreamCipherTransformation"
decl_stmt|;
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
name|DefaultStreamCachingStrategy
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|camelContext
specifier|private
name|CamelContext
name|camelContext
decl_stmt|;
DECL|field|enabled
specifier|private
name|boolean
name|enabled
decl_stmt|;
DECL|field|spoolDirectory
specifier|private
name|File
name|spoolDirectory
decl_stmt|;
DECL|field|spoolThreshold
specifier|private
name|long
name|spoolThreshold
init|=
name|StreamCache
operator|.
name|DEFAULT_SPOOL_THRESHOLD
decl_stmt|;
DECL|field|spoolChiper
specifier|private
name|String
name|spoolChiper
decl_stmt|;
DECL|field|bufferSize
specifier|private
name|int
name|bufferSize
init|=
name|IOHelper
operator|.
name|DEFAULT_BUFFER_SIZE
decl_stmt|;
DECL|field|removeSpoolDirectoryWhenStopping
specifier|private
name|boolean
name|removeSpoolDirectoryWhenStopping
init|=
literal|true
decl_stmt|;
DECL|method|getCamelContext ()
specifier|public
name|CamelContext
name|getCamelContext
parameter_list|()
block|{
return|return
name|camelContext
return|;
block|}
DECL|method|setCamelContext (CamelContext camelContext)
specifier|public
name|void
name|setCamelContext
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|this
operator|.
name|camelContext
operator|=
name|camelContext
expr_stmt|;
block|}
DECL|method|isEnabled ()
specifier|public
name|boolean
name|isEnabled
parameter_list|()
block|{
return|return
name|enabled
return|;
block|}
DECL|method|setEnabled (boolean enabled)
specifier|public
name|void
name|setEnabled
parameter_list|(
name|boolean
name|enabled
parameter_list|)
block|{
name|this
operator|.
name|enabled
operator|=
name|enabled
expr_stmt|;
block|}
DECL|method|setSpoolDirectory (String path)
specifier|public
name|void
name|setSpoolDirectory
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|this
operator|.
name|spoolDirectory
operator|=
operator|new
name|File
argument_list|(
name|path
argument_list|)
expr_stmt|;
block|}
DECL|method|setSpoolDirectory (File path)
specifier|public
name|void
name|setSpoolDirectory
parameter_list|(
name|File
name|path
parameter_list|)
block|{
name|this
operator|.
name|spoolDirectory
operator|=
name|path
expr_stmt|;
block|}
DECL|method|getSpoolDirectory ()
specifier|public
name|File
name|getSpoolDirectory
parameter_list|()
block|{
return|return
name|spoolDirectory
return|;
block|}
DECL|method|getSpoolThreshold ()
specifier|public
name|long
name|getSpoolThreshold
parameter_list|()
block|{
return|return
name|spoolThreshold
return|;
block|}
DECL|method|setSpoolThreshold (long spoolThreshold)
specifier|public
name|void
name|setSpoolThreshold
parameter_list|(
name|long
name|spoolThreshold
parameter_list|)
block|{
name|this
operator|.
name|spoolThreshold
operator|=
name|spoolThreshold
expr_stmt|;
block|}
DECL|method|getSpoolChiper ()
specifier|public
name|String
name|getSpoolChiper
parameter_list|()
block|{
return|return
name|spoolChiper
return|;
block|}
DECL|method|setSpoolChiper (String spoolChiper)
specifier|public
name|void
name|setSpoolChiper
parameter_list|(
name|String
name|spoolChiper
parameter_list|)
block|{
name|this
operator|.
name|spoolChiper
operator|=
name|spoolChiper
expr_stmt|;
block|}
DECL|method|getBufferSize ()
specifier|public
name|int
name|getBufferSize
parameter_list|()
block|{
return|return
name|bufferSize
return|;
block|}
DECL|method|setBufferSize (int bufferSize)
specifier|public
name|void
name|setBufferSize
parameter_list|(
name|int
name|bufferSize
parameter_list|)
block|{
name|this
operator|.
name|bufferSize
operator|=
name|bufferSize
expr_stmt|;
block|}
DECL|method|isRemoveSpoolDirectoryWhenStopping ()
specifier|public
name|boolean
name|isRemoveSpoolDirectoryWhenStopping
parameter_list|()
block|{
return|return
name|removeSpoolDirectoryWhenStopping
return|;
block|}
DECL|method|setRemoveSpoolDirectoryWhenStopping (boolean removeSpoolDirectoryWhenStopping)
specifier|public
name|void
name|setRemoveSpoolDirectoryWhenStopping
parameter_list|(
name|boolean
name|removeSpoolDirectoryWhenStopping
parameter_list|)
block|{
name|this
operator|.
name|removeSpoolDirectoryWhenStopping
operator|=
name|removeSpoolDirectoryWhenStopping
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
if|if
condition|(
operator|!
name|enabled
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"StreamCaching is not enabled"
argument_list|)
expr_stmt|;
return|return;
block|}
name|String
name|bufferSize
init|=
name|camelContext
operator|.
name|getProperty
argument_list|(
name|BUFFER_SIZE
argument_list|)
decl_stmt|;
name|String
name|hold
init|=
name|camelContext
operator|.
name|getProperty
argument_list|(
name|THRESHOLD
argument_list|)
decl_stmt|;
name|String
name|chiper
init|=
name|camelContext
operator|.
name|getProperty
argument_list|(
name|CIPHER_TRANSFORMATION
argument_list|)
decl_stmt|;
name|String
name|dir
init|=
name|camelContext
operator|.
name|getProperty
argument_list|(
name|TEMP_DIR
argument_list|)
decl_stmt|;
name|boolean
name|warn
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|bufferSize
operator|!=
literal|null
condition|)
block|{
name|warn
operator|=
literal|true
expr_stmt|;
name|this
operator|.
name|bufferSize
operator|=
name|camelContext
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|Integer
operator|.
name|class
argument_list|,
name|bufferSize
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|hold
operator|!=
literal|null
condition|)
block|{
name|warn
operator|=
literal|true
expr_stmt|;
name|this
operator|.
name|spoolThreshold
operator|=
name|camelContext
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|Long
operator|.
name|class
argument_list|,
name|hold
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|chiper
operator|!=
literal|null
condition|)
block|{
name|warn
operator|=
literal|true
expr_stmt|;
name|this
operator|.
name|spoolChiper
operator|=
name|chiper
expr_stmt|;
block|}
if|if
condition|(
name|dir
operator|!=
literal|null
condition|)
block|{
name|warn
operator|=
literal|true
expr_stmt|;
name|this
operator|.
name|spoolDirectory
operator|=
name|camelContext
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|File
operator|.
name|class
argument_list|,
name|dir
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|warn
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Configuring of StreamCaching using CamelContext properties is deprecated - use StreamCachingStrategy instead."
argument_list|)
expr_stmt|;
block|}
name|LOG
operator|.
name|info
argument_list|(
literal|"StreamCaching in use with {}"
argument_list|,
name|this
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
comment|// create random temporary directory if none has been created
if|if
condition|(
name|spoolDirectory
operator|==
literal|null
condition|)
block|{
name|spoolDirectory
operator|=
name|FileUtil
operator|.
name|createNewTempDir
argument_list|()
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Created temporary spool directory {}"
argument_list|,
name|spoolDirectory
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
operator|!
name|spoolDirectory
operator|.
name|exists
argument_list|()
condition|)
block|{
name|boolean
name|created
init|=
name|spoolDirectory
operator|.
name|mkdirs
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|created
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Cannot create spool directory {}"
argument_list|,
name|spoolDirectory
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Created spool directory {}"
argument_list|,
name|spoolDirectory
argument_list|)
expr_stmt|;
block|}
block|}
block|}
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
if|if
condition|(
name|spoolDirectory
operator|!=
literal|null
operator|&&
name|isRemoveSpoolDirectoryWhenStopping
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Removing spool directory {}"
argument_list|,
name|spoolDirectory
argument_list|)
expr_stmt|;
name|FileUtil
operator|.
name|removeDir
argument_list|(
name|spoolDirectory
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"DefaultStreamCachingStrategy["
operator|+
literal|"spoolDirectory="
operator|+
name|spoolDirectory
operator|+
literal|", spoolThreshold="
operator|+
name|spoolThreshold
operator|+
literal|", spoolChiper="
operator|+
name|spoolChiper
operator|+
literal|", bufferSize="
operator|+
name|bufferSize
operator|+
literal|"]"
return|;
block|}
block|}
end_class

end_unit

