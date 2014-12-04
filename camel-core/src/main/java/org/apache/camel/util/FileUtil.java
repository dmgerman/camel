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
name|io
operator|.
name|File
import|;
end_import

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
name|FileOutputStream
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
name|nio
operator|.
name|channels
operator|.
name|FileChannel
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Locale
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Random
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Stack
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
comment|/**  * File utilities.  */
end_comment

begin_class
DECL|class|FileUtil
specifier|public
specifier|final
class|class
name|FileUtil
block|{
DECL|field|BUFFER_SIZE
specifier|public
specifier|static
specifier|final
name|int
name|BUFFER_SIZE
init|=
literal|128
operator|*
literal|1024
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
name|FileUtil
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|RETRY_SLEEP_MILLIS
specifier|private
specifier|static
specifier|final
name|int
name|RETRY_SLEEP_MILLIS
init|=
literal|10
decl_stmt|;
comment|/**      * The System property key for the user directory.      */
DECL|field|USER_DIR_KEY
specifier|private
specifier|static
specifier|final
name|String
name|USER_DIR_KEY
init|=
literal|"user.dir"
decl_stmt|;
DECL|field|USER_DIR
specifier|private
specifier|static
specifier|final
name|File
name|USER_DIR
init|=
operator|new
name|File
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
name|USER_DIR_KEY
argument_list|)
argument_list|)
decl_stmt|;
DECL|field|defaultTempDir
specifier|private
specifier|static
name|File
name|defaultTempDir
decl_stmt|;
DECL|field|shutdownHook
specifier|private
specifier|static
name|Thread
name|shutdownHook
decl_stmt|;
DECL|field|windowsOs
specifier|private
specifier|static
name|boolean
name|windowsOs
init|=
name|initWindowsOs
argument_list|()
decl_stmt|;
DECL|method|FileUtil ()
specifier|private
name|FileUtil
parameter_list|()
block|{
comment|// Utils method
block|}
DECL|method|initWindowsOs ()
specifier|private
specifier|static
name|boolean
name|initWindowsOs
parameter_list|()
block|{
comment|// initialize once as System.getProperty is not fast
name|String
name|osName
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"os.name"
argument_list|)
operator|.
name|toLowerCase
argument_list|(
name|Locale
operator|.
name|US
argument_list|)
decl_stmt|;
return|return
name|osName
operator|.
name|contains
argument_list|(
literal|"windows"
argument_list|)
return|;
block|}
DECL|method|getUserDir ()
specifier|public
specifier|static
name|File
name|getUserDir
parameter_list|()
block|{
return|return
name|USER_DIR
return|;
block|}
comment|/**      * Normalizes the path to cater for Windows and other platforms      */
DECL|method|normalizePath (String path)
specifier|public
specifier|static
name|String
name|normalizePath
parameter_list|(
name|String
name|path
parameter_list|)
block|{
if|if
condition|(
name|path
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
if|if
condition|(
name|isWindows
argument_list|()
condition|)
block|{
comment|// special handling for Windows where we need to convert / to \\
return|return
name|path
operator|.
name|replace
argument_list|(
literal|'/'
argument_list|,
literal|'\\'
argument_list|)
return|;
block|}
else|else
block|{
comment|// for other systems make sure we use / as separators
return|return
name|path
operator|.
name|replace
argument_list|(
literal|'\\'
argument_list|,
literal|'/'
argument_list|)
return|;
block|}
block|}
comment|/**      * Returns true, if the OS is windows      */
DECL|method|isWindows ()
specifier|public
specifier|static
name|boolean
name|isWindows
parameter_list|()
block|{
return|return
name|windowsOs
return|;
block|}
annotation|@
name|Deprecated
DECL|method|createTempFile (String prefix, String suffix)
specifier|public
specifier|static
name|File
name|createTempFile
parameter_list|(
name|String
name|prefix
parameter_list|,
name|String
name|suffix
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|createTempFile
argument_list|(
name|prefix
argument_list|,
name|suffix
argument_list|,
literal|null
argument_list|)
return|;
block|}
DECL|method|createTempFile (String prefix, String suffix, File parentDir)
specifier|public
specifier|static
name|File
name|createTempFile
parameter_list|(
name|String
name|prefix
parameter_list|,
name|String
name|suffix
parameter_list|,
name|File
name|parentDir
parameter_list|)
throws|throws
name|IOException
block|{
comment|// TODO: parentDir should be mandatory
name|File
name|parent
init|=
operator|(
name|parentDir
operator|==
literal|null
operator|)
condition|?
name|getDefaultTempDir
argument_list|()
else|:
name|parentDir
decl_stmt|;
if|if
condition|(
name|suffix
operator|==
literal|null
condition|)
block|{
name|suffix
operator|=
literal|".tmp"
expr_stmt|;
block|}
if|if
condition|(
name|prefix
operator|==
literal|null
condition|)
block|{
name|prefix
operator|=
literal|"camel"
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|prefix
operator|.
name|length
argument_list|()
operator|<
literal|3
condition|)
block|{
name|prefix
operator|=
name|prefix
operator|+
literal|"camel"
expr_stmt|;
block|}
comment|// create parent folder
name|parent
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
return|return
name|File
operator|.
name|createTempFile
argument_list|(
name|prefix
argument_list|,
name|suffix
argument_list|,
name|parent
argument_list|)
return|;
block|}
comment|/**      * Strip any leading separators      */
DECL|method|stripLeadingSeparator (String name)
specifier|public
specifier|static
name|String
name|stripLeadingSeparator
parameter_list|(
name|String
name|name
parameter_list|)
block|{
if|if
condition|(
name|name
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
while|while
condition|(
name|name
operator|.
name|startsWith
argument_list|(
literal|"/"
argument_list|)
operator|||
name|name
operator|.
name|startsWith
argument_list|(
name|File
operator|.
name|separator
argument_list|)
condition|)
block|{
name|name
operator|=
name|name
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
return|return
name|name
return|;
block|}
comment|/**      * Does the name start with a leading separator      */
DECL|method|hasLeadingSeparator (String name)
specifier|public
specifier|static
name|boolean
name|hasLeadingSeparator
parameter_list|(
name|String
name|name
parameter_list|)
block|{
if|if
condition|(
name|name
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|name
operator|.
name|startsWith
argument_list|(
literal|"/"
argument_list|)
operator|||
name|name
operator|.
name|startsWith
argument_list|(
name|File
operator|.
name|separator
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
return|return
literal|false
return|;
block|}
comment|/**      * Strip first leading separator      */
DECL|method|stripFirstLeadingSeparator (String name)
specifier|public
specifier|static
name|String
name|stripFirstLeadingSeparator
parameter_list|(
name|String
name|name
parameter_list|)
block|{
if|if
condition|(
name|name
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
if|if
condition|(
name|name
operator|.
name|startsWith
argument_list|(
literal|"/"
argument_list|)
operator|||
name|name
operator|.
name|startsWith
argument_list|(
name|File
operator|.
name|separator
argument_list|)
condition|)
block|{
name|name
operator|=
name|name
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
return|return
name|name
return|;
block|}
comment|/**      * Strip any trailing separators      */
DECL|method|stripTrailingSeparator (String name)
specifier|public
specifier|static
name|String
name|stripTrailingSeparator
parameter_list|(
name|String
name|name
parameter_list|)
block|{
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|name
argument_list|)
condition|)
block|{
return|return
name|name
return|;
block|}
name|String
name|s
init|=
name|name
decl_stmt|;
comment|// there must be some leading text, as we should only remove trailing separators
while|while
condition|(
name|s
operator|.
name|endsWith
argument_list|(
literal|"/"
argument_list|)
operator|||
name|s
operator|.
name|endsWith
argument_list|(
name|File
operator|.
name|separator
argument_list|)
condition|)
block|{
name|s
operator|=
name|s
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|s
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
comment|// if the string is empty, that means there was only trailing slashes, and no leading text
comment|// and so we should then return the original name as is
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|s
argument_list|)
condition|)
block|{
return|return
name|name
return|;
block|}
else|else
block|{
comment|// return without trailing slashes
return|return
name|s
return|;
block|}
block|}
comment|/**      * Strips any leading paths      */
DECL|method|stripPath (String name)
specifier|public
specifier|static
name|String
name|stripPath
parameter_list|(
name|String
name|name
parameter_list|)
block|{
if|if
condition|(
name|name
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|int
name|posUnix
init|=
name|name
operator|.
name|lastIndexOf
argument_list|(
literal|'/'
argument_list|)
decl_stmt|;
name|int
name|posWin
init|=
name|name
operator|.
name|lastIndexOf
argument_list|(
literal|'\\'
argument_list|)
decl_stmt|;
name|int
name|pos
init|=
name|Math
operator|.
name|max
argument_list|(
name|posUnix
argument_list|,
name|posWin
argument_list|)
decl_stmt|;
if|if
condition|(
name|pos
operator|!=
operator|-
literal|1
condition|)
block|{
return|return
name|name
operator|.
name|substring
argument_list|(
name|pos
operator|+
literal|1
argument_list|)
return|;
block|}
return|return
name|name
return|;
block|}
DECL|method|stripExt (String name)
specifier|public
specifier|static
name|String
name|stripExt
parameter_list|(
name|String
name|name
parameter_list|)
block|{
if|if
condition|(
name|name
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|int
name|pos
init|=
name|name
operator|.
name|lastIndexOf
argument_list|(
literal|'.'
argument_list|)
decl_stmt|;
if|if
condition|(
name|pos
operator|!=
operator|-
literal|1
condition|)
block|{
return|return
name|name
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|pos
argument_list|)
return|;
block|}
return|return
name|name
return|;
block|}
comment|/**      * Returns only the leading path (returns<tt>null</tt> if no path)      */
DECL|method|onlyPath (String name)
specifier|public
specifier|static
name|String
name|onlyPath
parameter_list|(
name|String
name|name
parameter_list|)
block|{
if|if
condition|(
name|name
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|int
name|posUnix
init|=
name|name
operator|.
name|lastIndexOf
argument_list|(
literal|'/'
argument_list|)
decl_stmt|;
name|int
name|posWin
init|=
name|name
operator|.
name|lastIndexOf
argument_list|(
literal|'\\'
argument_list|)
decl_stmt|;
name|int
name|pos
init|=
name|Math
operator|.
name|max
argument_list|(
name|posUnix
argument_list|,
name|posWin
argument_list|)
decl_stmt|;
if|if
condition|(
name|pos
operator|>
literal|0
condition|)
block|{
return|return
name|name
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|pos
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|pos
operator|==
literal|0
condition|)
block|{
comment|// name is in the root path, so extract the path as the first char
return|return
name|name
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
literal|1
argument_list|)
return|;
block|}
comment|// no path in name
return|return
literal|null
return|;
block|}
comment|/**      * Compacts a path by stacking it and reducing<tt>..</tt>,      * and uses OS specific file separators (eg {@link java.io.File#separator}).      */
DECL|method|compactPath (String path)
specifier|public
specifier|static
name|String
name|compactPath
parameter_list|(
name|String
name|path
parameter_list|)
block|{
return|return
name|compactPath
argument_list|(
name|path
argument_list|,
name|File
operator|.
name|separatorChar
argument_list|)
return|;
block|}
comment|/**      * Compacts a path by stacking it and reducing<tt>..</tt>,      * and uses the given separator.      */
DECL|method|compactPath (String path, char separator)
specifier|public
specifier|static
name|String
name|compactPath
parameter_list|(
name|String
name|path
parameter_list|,
name|char
name|separator
parameter_list|)
block|{
if|if
condition|(
name|path
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
comment|// only normalize if contains a path separator
if|if
condition|(
name|path
operator|.
name|indexOf
argument_list|(
literal|'/'
argument_list|)
operator|==
operator|-
literal|1
operator|&&
name|path
operator|.
name|indexOf
argument_list|(
literal|'\\'
argument_list|)
operator|==
operator|-
literal|1
condition|)
block|{
return|return
name|path
return|;
block|}
comment|// need to normalize path before compacting
name|path
operator|=
name|normalizePath
argument_list|(
name|path
argument_list|)
expr_stmt|;
comment|// preserve ending slash if given in input path
name|boolean
name|endsWithSlash
init|=
name|path
operator|.
name|endsWith
argument_list|(
literal|"/"
argument_list|)
operator|||
name|path
operator|.
name|endsWith
argument_list|(
literal|"\\"
argument_list|)
decl_stmt|;
comment|// preserve starting slash if given in input path
name|boolean
name|startsWithSlash
init|=
name|path
operator|.
name|startsWith
argument_list|(
literal|"/"
argument_list|)
operator|||
name|path
operator|.
name|startsWith
argument_list|(
literal|"\\"
argument_list|)
decl_stmt|;
name|Stack
argument_list|<
name|String
argument_list|>
name|stack
init|=
operator|new
name|Stack
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
comment|// separator can either be windows or unix style
name|String
name|separatorRegex
init|=
literal|"\\\\|/"
decl_stmt|;
name|String
index|[]
name|parts
init|=
name|path
operator|.
name|split
argument_list|(
name|separatorRegex
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|part
range|:
name|parts
control|)
block|{
if|if
condition|(
name|part
operator|.
name|equals
argument_list|(
literal|".."
argument_list|)
operator|&&
operator|!
name|stack
operator|.
name|isEmpty
argument_list|()
operator|&&
operator|!
literal|".."
operator|.
name|equals
argument_list|(
name|stack
operator|.
name|peek
argument_list|()
argument_list|)
condition|)
block|{
comment|// only pop if there is a previous path, which is not a ".." path either
name|stack
operator|.
name|pop
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|part
operator|.
name|equals
argument_list|(
literal|"."
argument_list|)
operator|||
name|part
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
comment|// do nothing because we don't want a path like foo/./bar or foo//bar
block|}
else|else
block|{
name|stack
operator|.
name|push
argument_list|(
name|part
argument_list|)
expr_stmt|;
block|}
block|}
comment|// build path based on stack
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
if|if
condition|(
name|startsWithSlash
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|separator
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|Iterator
argument_list|<
name|String
argument_list|>
name|it
init|=
name|stack
operator|.
name|iterator
argument_list|()
init|;
name|it
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|it
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|separator
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|endsWithSlash
operator|&&
name|stack
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|separator
argument_list|)
expr_stmt|;
block|}
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
annotation|@
name|Deprecated
DECL|method|getDefaultTempDir ()
specifier|private
specifier|static
specifier|synchronized
name|File
name|getDefaultTempDir
parameter_list|()
block|{
if|if
condition|(
name|defaultTempDir
operator|!=
literal|null
operator|&&
name|defaultTempDir
operator|.
name|exists
argument_list|()
condition|)
block|{
return|return
name|defaultTempDir
return|;
block|}
name|defaultTempDir
operator|=
name|createNewTempDir
argument_list|()
expr_stmt|;
comment|// create shutdown hook to remove the temp dir
name|shutdownHook
operator|=
operator|new
name|Thread
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|run
parameter_list|()
block|{
name|removeDir
argument_list|(
name|defaultTempDir
argument_list|)
expr_stmt|;
block|}
block|}
expr_stmt|;
name|Runtime
operator|.
name|getRuntime
argument_list|()
operator|.
name|addShutdownHook
argument_list|(
name|shutdownHook
argument_list|)
expr_stmt|;
return|return
name|defaultTempDir
return|;
block|}
comment|/**      * Creates a new temporary directory in the<tt>java.io.tmpdir</tt> directory.      */
annotation|@
name|Deprecated
DECL|method|createNewTempDir ()
specifier|private
specifier|static
name|File
name|createNewTempDir
parameter_list|()
block|{
name|String
name|s
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"java.io.tmpdir"
argument_list|)
decl_stmt|;
name|File
name|checkExists
init|=
operator|new
name|File
argument_list|(
name|s
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|checkExists
operator|.
name|exists
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"The directory "
operator|+
name|checkExists
operator|.
name|getAbsolutePath
argument_list|()
operator|+
literal|" does not exist, please set java.io.tempdir"
operator|+
literal|" to an existing directory"
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
name|checkExists
operator|.
name|canWrite
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"The directory "
operator|+
name|checkExists
operator|.
name|getAbsolutePath
argument_list|()
operator|+
literal|" is not writable, please set java.io.tempdir"
operator|+
literal|" to a writable directory"
argument_list|)
throw|;
block|}
comment|// create a sub folder with a random number
name|Random
name|ran
init|=
operator|new
name|Random
argument_list|()
decl_stmt|;
name|int
name|x
init|=
name|ran
operator|.
name|nextInt
argument_list|(
literal|1000000
argument_list|)
decl_stmt|;
name|File
name|f
init|=
operator|new
name|File
argument_list|(
name|s
argument_list|,
literal|"camel-tmp-"
operator|+
name|x
argument_list|)
decl_stmt|;
name|int
name|count
init|=
literal|0
decl_stmt|;
comment|// Let us just try 100 times to avoid the infinite loop
while|while
condition|(
operator|!
name|f
operator|.
name|mkdir
argument_list|()
condition|)
block|{
name|count
operator|++
expr_stmt|;
if|if
condition|(
name|count
operator|>=
literal|100
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Camel cannot a temp directory from"
operator|+
name|checkExists
operator|.
name|getAbsolutePath
argument_list|()
operator|+
literal|" 100 times , please set java.io.tempdir"
operator|+
literal|" to a writable directory"
argument_list|)
throw|;
block|}
name|x
operator|=
name|ran
operator|.
name|nextInt
argument_list|(
literal|1000000
argument_list|)
expr_stmt|;
name|f
operator|=
operator|new
name|File
argument_list|(
name|s
argument_list|,
literal|"camel-tmp-"
operator|+
name|x
argument_list|)
expr_stmt|;
block|}
return|return
name|f
return|;
block|}
comment|/**      * Shutdown and cleanup the temporary directory and removes any shutdown hooks in use.      */
annotation|@
name|Deprecated
DECL|method|shutdown ()
specifier|public
specifier|static
specifier|synchronized
name|void
name|shutdown
parameter_list|()
block|{
if|if
condition|(
name|defaultTempDir
operator|!=
literal|null
operator|&&
name|defaultTempDir
operator|.
name|exists
argument_list|()
condition|)
block|{
name|removeDir
argument_list|(
name|defaultTempDir
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|shutdownHook
operator|!=
literal|null
condition|)
block|{
name|Runtime
operator|.
name|getRuntime
argument_list|()
operator|.
name|removeShutdownHook
argument_list|(
name|shutdownHook
argument_list|)
expr_stmt|;
name|shutdownHook
operator|=
literal|null
expr_stmt|;
block|}
block|}
DECL|method|removeDir (File d)
specifier|public
specifier|static
name|void
name|removeDir
parameter_list|(
name|File
name|d
parameter_list|)
block|{
name|String
index|[]
name|list
init|=
name|d
operator|.
name|list
argument_list|()
decl_stmt|;
if|if
condition|(
name|list
operator|==
literal|null
condition|)
block|{
name|list
operator|=
operator|new
name|String
index|[
literal|0
index|]
expr_stmt|;
block|}
for|for
control|(
name|String
name|s
range|:
name|list
control|)
block|{
name|File
name|f
init|=
operator|new
name|File
argument_list|(
name|d
argument_list|,
name|s
argument_list|)
decl_stmt|;
if|if
condition|(
name|f
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
name|removeDir
argument_list|(
name|f
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|delete
argument_list|(
name|f
argument_list|)
expr_stmt|;
block|}
block|}
name|delete
argument_list|(
name|d
argument_list|)
expr_stmt|;
block|}
DECL|method|delete (File f)
specifier|private
specifier|static
name|void
name|delete
parameter_list|(
name|File
name|f
parameter_list|)
block|{
if|if
condition|(
operator|!
name|f
operator|.
name|delete
argument_list|()
condition|)
block|{
if|if
condition|(
name|isWindows
argument_list|()
condition|)
block|{
name|System
operator|.
name|gc
argument_list|()
expr_stmt|;
block|}
try|try
block|{
name|Thread
operator|.
name|sleep
argument_list|(
name|RETRY_SLEEP_MILLIS
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|ex
parameter_list|)
block|{
comment|// Ignore Exception
block|}
if|if
condition|(
operator|!
name|f
operator|.
name|delete
argument_list|()
condition|)
block|{
name|f
operator|.
name|deleteOnExit
argument_list|()
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Renames a file.      *      * @param from the from file      * @param to   the to file      * @param copyAndDeleteOnRenameFail whether to fallback and do copy and delete, if renameTo fails      * @return<tt>true</tt> if the file was renamed, otherwise<tt>false</tt>      * @throws java.io.IOException is thrown if error renaming file      */
DECL|method|renameFile (File from, File to, boolean copyAndDeleteOnRenameFail)
specifier|public
specifier|static
name|boolean
name|renameFile
parameter_list|(
name|File
name|from
parameter_list|,
name|File
name|to
parameter_list|,
name|boolean
name|copyAndDeleteOnRenameFail
parameter_list|)
throws|throws
name|IOException
block|{
comment|// do not try to rename non existing files
if|if
condition|(
operator|!
name|from
operator|.
name|exists
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
comment|// some OS such as Windows can have problem doing rename IO operations so we may need to
comment|// retry a couple of times to let it work
name|boolean
name|renamed
init|=
literal|false
decl_stmt|;
name|int
name|count
init|=
literal|0
decl_stmt|;
while|while
condition|(
operator|!
name|renamed
operator|&&
name|count
operator|<
literal|3
condition|)
block|{
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
operator|&&
name|count
operator|>
literal|0
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Retrying attempt {} to rename file from: {} to: {}"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|count
block|,
name|from
block|,
name|to
block|}
argument_list|)
expr_stmt|;
block|}
name|renamed
operator|=
name|from
operator|.
name|renameTo
argument_list|(
name|to
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|renamed
operator|&&
name|count
operator|>
literal|0
condition|)
block|{
try|try
block|{
name|Thread
operator|.
name|sleep
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
comment|// ignore
block|}
block|}
name|count
operator|++
expr_stmt|;
block|}
comment|// we could not rename using renameTo, so lets fallback and do a copy/delete approach.
comment|// for example if you move files between different file systems (linux -> windows etc.)
if|if
condition|(
operator|!
name|renamed
operator|&&
name|copyAndDeleteOnRenameFail
condition|)
block|{
comment|// now do a copy and delete as all rename attempts failed
name|LOG
operator|.
name|debug
argument_list|(
literal|"Cannot rename file from: {} to: {}, will now use a copy/delete approach instead"
argument_list|,
name|from
argument_list|,
name|to
argument_list|)
expr_stmt|;
name|renamed
operator|=
name|renameFileUsingCopy
argument_list|(
name|from
argument_list|,
name|to
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
operator|&&
name|count
operator|>
literal|0
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Tried {} to rename file: {} to: {} with result: {}"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|count
block|,
name|from
block|,
name|to
block|,
name|renamed
block|}
argument_list|)
expr_stmt|;
block|}
return|return
name|renamed
return|;
block|}
comment|/**      * Rename file using copy and delete strategy. This is primarily used in      * environments where the regular rename operation is unreliable.      *       * @param from the file to be renamed      * @param to the new target file      * @return<tt>true</tt> if the file was renamed successfully, otherwise<tt>false</tt>      * @throws IOException If an I/O error occurs during copy or delete operations.      */
DECL|method|renameFileUsingCopy (File from, File to)
specifier|public
specifier|static
name|boolean
name|renameFileUsingCopy
parameter_list|(
name|File
name|from
parameter_list|,
name|File
name|to
parameter_list|)
throws|throws
name|IOException
block|{
comment|// do not try to rename non existing files
if|if
condition|(
operator|!
name|from
operator|.
name|exists
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
name|LOG
operator|.
name|debug
argument_list|(
literal|"Rename file '{}' to '{}' using copy/delete strategy."
argument_list|,
name|from
argument_list|,
name|to
argument_list|)
expr_stmt|;
name|copyFile
argument_list|(
name|from
argument_list|,
name|to
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|deleteFile
argument_list|(
name|from
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Renaming file from '"
operator|+
name|from
operator|+
literal|"' to '"
operator|+
name|to
operator|+
literal|"' failed: Cannot delete file '"
operator|+
name|from
operator|+
literal|"' after copy succeeded"
argument_list|)
throw|;
block|}
return|return
literal|true
return|;
block|}
DECL|method|copyFile (File from, File to)
specifier|public
specifier|static
name|void
name|copyFile
parameter_list|(
name|File
name|from
parameter_list|,
name|File
name|to
parameter_list|)
throws|throws
name|IOException
block|{
name|FileChannel
name|in
init|=
literal|null
decl_stmt|;
name|FileChannel
name|out
init|=
literal|null
decl_stmt|;
try|try
block|{
name|in
operator|=
operator|new
name|FileInputStream
argument_list|(
name|from
argument_list|)
operator|.
name|getChannel
argument_list|()
expr_stmt|;
name|out
operator|=
operator|new
name|FileOutputStream
argument_list|(
name|to
argument_list|)
operator|.
name|getChannel
argument_list|()
expr_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Using FileChannel to copy from: "
operator|+
name|in
operator|+
literal|" to: "
operator|+
name|out
argument_list|)
expr_stmt|;
block|}
name|long
name|size
init|=
name|in
operator|.
name|size
argument_list|()
decl_stmt|;
name|long
name|position
init|=
literal|0
decl_stmt|;
while|while
condition|(
name|position
operator|<
name|size
condition|)
block|{
name|position
operator|+=
name|in
operator|.
name|transferTo
argument_list|(
name|position
argument_list|,
name|BUFFER_SIZE
argument_list|,
name|out
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|IOHelper
operator|.
name|close
argument_list|(
name|in
argument_list|,
name|from
operator|.
name|getName
argument_list|()
argument_list|,
name|LOG
argument_list|)
expr_stmt|;
name|IOHelper
operator|.
name|close
argument_list|(
name|out
argument_list|,
name|to
operator|.
name|getName
argument_list|()
argument_list|,
name|LOG
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|deleteFile (File file)
specifier|public
specifier|static
name|boolean
name|deleteFile
parameter_list|(
name|File
name|file
parameter_list|)
block|{
comment|// do not try to delete non existing files
if|if
condition|(
operator|!
name|file
operator|.
name|exists
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
comment|// some OS such as Windows can have problem doing delete IO operations so we may need to
comment|// retry a couple of times to let it work
name|boolean
name|deleted
init|=
literal|false
decl_stmt|;
name|int
name|count
init|=
literal|0
decl_stmt|;
while|while
condition|(
operator|!
name|deleted
operator|&&
name|count
operator|<
literal|3
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Retrying attempt {} to delete file: {}"
argument_list|,
name|count
argument_list|,
name|file
argument_list|)
expr_stmt|;
name|deleted
operator|=
name|file
operator|.
name|delete
argument_list|()
expr_stmt|;
if|if
condition|(
operator|!
name|deleted
operator|&&
name|count
operator|>
literal|0
condition|)
block|{
try|try
block|{
name|Thread
operator|.
name|sleep
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
comment|// ignore
block|}
block|}
name|count
operator|++
expr_stmt|;
block|}
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
operator|&&
name|count
operator|>
literal|0
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Tried {} to delete file: {} with result: {}"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|count
block|,
name|file
block|,
name|deleted
block|}
argument_list|)
expr_stmt|;
block|}
return|return
name|deleted
return|;
block|}
comment|/**      * Is the given file an absolute file.      *<p/>      * Will also work around issue on Windows to consider files on Windows starting with a \      * as absolute files. This makes the logic consistent across all OS platforms.      *      * @param file  the file      * @return<tt>true</ff> if its an absolute path,<tt>false</tt> otherwise.      */
DECL|method|isAbsolute (File file)
specifier|public
specifier|static
name|boolean
name|isAbsolute
parameter_list|(
name|File
name|file
parameter_list|)
block|{
if|if
condition|(
name|isWindows
argument_list|()
condition|)
block|{
comment|// special for windows
name|String
name|path
init|=
name|file
operator|.
name|getPath
argument_list|()
decl_stmt|;
if|if
condition|(
name|path
operator|.
name|startsWith
argument_list|(
name|File
operator|.
name|separator
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
name|file
operator|.
name|isAbsolute
argument_list|()
return|;
block|}
comment|/**      * Creates a new file.      *      * @param file the file      * @return<tt>true</tt> if created a new file,<tt>false</tt> otherwise      * @throws IOException is thrown if error creating the new file      */
DECL|method|createNewFile (File file)
specifier|public
specifier|static
name|boolean
name|createNewFile
parameter_list|(
name|File
name|file
parameter_list|)
throws|throws
name|IOException
block|{
try|try
block|{
return|return
name|file
operator|.
name|createNewFile
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
if|if
condition|(
name|file
operator|.
name|exists
argument_list|()
condition|)
block|{
return|return
literal|true
return|;
block|}
else|else
block|{
throw|throw
name|e
throw|;
block|}
block|}
block|}
block|}
end_class

end_unit

