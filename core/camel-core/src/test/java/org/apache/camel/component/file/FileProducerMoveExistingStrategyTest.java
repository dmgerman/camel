begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.file
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|file
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
name|ContextTestSupport
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
name|component
operator|.
name|file
operator|.
name|strategy
operator|.
name|FileMoveExistingStrategy
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
name|impl
operator|.
name|JndiRegistry
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
name|ObjectHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
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
comment|/**  *  */
end_comment

begin_class
DECL|class|FileProducerMoveExistingStrategyTest
specifier|public
class|class
name|FileProducerMoveExistingStrategyTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|myStrategy
specifier|private
name|MyStrategy
name|myStrategy
init|=
operator|new
name|MyStrategy
argument_list|()
decl_stmt|;
annotation|@
name|Override
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|deleteDirectory
argument_list|(
literal|"target/data/file"
argument_list|)
expr_stmt|;
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createRegistry ()
specifier|protected
name|JndiRegistry
name|createRegistry
parameter_list|()
throws|throws
name|Exception
block|{
name|JndiRegistry
name|jndi
init|=
name|super
operator|.
name|createRegistry
argument_list|()
decl_stmt|;
name|jndi
operator|.
name|bind
argument_list|(
literal|"myStrategy"
argument_list|,
name|myStrategy
argument_list|)
expr_stmt|;
return|return
name|jndi
return|;
block|}
annotation|@
name|Test
DECL|method|testExistingFileExists ()
specifier|public
name|void
name|testExistingFileExists
parameter_list|()
throws|throws
name|Exception
block|{
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"file://target/data/file?fileExist=Move&moveExisting=${file:parent}/renamed-${file:onlyname}&moveExistingFileStrategy=#myStrategy"
argument_list|,
literal|"Hello World"
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"hello.txt"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"file://target/data/file?fileExist=Move&moveExisting=${file:parent}/renamed-${file:onlyname}&moveExistingFileStrategy=#myStrategy"
argument_list|,
literal|"Bye Existing World 1"
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"hello.txt"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"file://target/data/file?fileExist=Move&moveExisting=${file:parent}/renamed-${file:onlyname}&moveExistingFileStrategy=#myStrategy"
argument_list|,
literal|"Bye Existing World 2"
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"hello.txt"
argument_list|)
expr_stmt|;
name|assertFileExists
argument_list|(
literal|"target/data/file/hello.txt"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Bye Existing World 2"
argument_list|,
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
operator|new
name|File
argument_list|(
literal|"target/data/file/hello.txt"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertFileExists
argument_list|(
literal|"target/data/file/renamed-hello2.txt"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Bye Existing World 1"
argument_list|,
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
operator|new
name|File
argument_list|(
literal|"target/data/file/renamed-hello2.txt"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertFileExists
argument_list|(
literal|"target/data/file/renamed-hello1.txt"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Hello World"
argument_list|,
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
operator|new
name|File
argument_list|(
literal|"target/data/file/renamed-hello1.txt"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|class|MyStrategy
specifier|private
specifier|static
class|class
name|MyStrategy
implements|implements
name|FileMoveExistingStrategy
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
name|FileMoveExistingStrategy
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|counter
specifier|private
name|int
name|counter
decl_stmt|;
annotation|@
name|Override
DECL|method|moveExistingFile (GenericFileEndpoint endpoint, GenericFileOperations operations, String fileName)
specifier|public
name|boolean
name|moveExistingFile
parameter_list|(
name|GenericFileEndpoint
name|endpoint
parameter_list|,
name|GenericFileOperations
name|operations
parameter_list|,
name|String
name|fileName
parameter_list|)
throws|throws
name|GenericFileOperationFailedException
block|{
comment|// need to evaluate using a dummy and simulate the file first, to
comment|// have access to all the file attributes
comment|// create a dummy exchange as Exchange is needed for expression
comment|// evaluation
comment|// we support only the following 3 tokens.
name|Exchange
name|dummy
init|=
name|endpoint
operator|.
name|createExchange
argument_list|()
decl_stmt|;
name|String
name|parent
init|=
name|FileUtil
operator|.
name|onlyPath
argument_list|(
name|fileName
argument_list|)
decl_stmt|;
name|String
name|onlyName
init|=
name|FileUtil
operator|.
name|stripPath
argument_list|(
name|fileName
argument_list|)
decl_stmt|;
name|dummy
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
name|fileName
argument_list|)
expr_stmt|;
name|dummy
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|FILE_NAME_ONLY
argument_list|,
name|onlyName
argument_list|)
expr_stmt|;
name|dummy
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|FILE_PARENT
argument_list|,
name|parent
argument_list|)
expr_stmt|;
name|String
name|to
init|=
name|endpoint
operator|.
name|getMoveExisting
argument_list|()
operator|.
name|evaluate
argument_list|(
name|dummy
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|counter
operator|++
expr_stmt|;
name|String
name|fileNameWithoutExtension
init|=
name|to
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|to
operator|.
name|lastIndexOf
argument_list|(
literal|'.'
argument_list|)
argument_list|)
operator|+
literal|""
operator|+
name|counter
decl_stmt|;
name|to
operator|=
name|fileNameWithoutExtension
operator|+
name|to
operator|.
name|substring
argument_list|(
name|to
operator|.
name|lastIndexOf
argument_list|(
literal|'.'
argument_list|)
argument_list|,
name|to
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
comment|// we must normalize it (to avoid having both \ and / in the name
comment|// which confuses java.io.File)
name|to
operator|=
name|FileUtil
operator|.
name|normalizePath
argument_list|(
name|to
argument_list|)
expr_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|to
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|GenericFileOperationFailedException
argument_list|(
literal|"moveExisting evaluated as empty String, cannot move existing file: "
operator|+
name|fileName
argument_list|)
throw|;
block|}
comment|// ensure any paths is created before we rename as the renamed file
comment|// may be in a different path (which may be non exiting)
comment|// use java.io.File to compute the file path
name|File
name|toFile
init|=
operator|new
name|File
argument_list|(
name|to
argument_list|)
decl_stmt|;
name|String
name|directory
init|=
name|toFile
operator|.
name|getParent
argument_list|()
decl_stmt|;
name|boolean
name|absolute
init|=
name|FileUtil
operator|.
name|isAbsolute
argument_list|(
name|toFile
argument_list|)
decl_stmt|;
if|if
condition|(
name|directory
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
operator|!
name|operations
operator|.
name|buildDirectory
argument_list|(
name|directory
argument_list|,
name|absolute
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Cannot build directory [{}] (could be because of denied permissions)"
argument_list|,
name|directory
argument_list|)
expr_stmt|;
block|}
block|}
comment|// deal if there already exists a file
if|if
condition|(
name|operations
operator|.
name|existsFile
argument_list|(
name|to
argument_list|)
condition|)
block|{
if|if
condition|(
name|endpoint
operator|.
name|isEagerDeleteTargetFile
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Deleting existing file: {}"
argument_list|,
name|to
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|operations
operator|.
name|deleteFile
argument_list|(
name|to
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|GenericFileOperationFailedException
argument_list|(
literal|"Cannot delete file: "
operator|+
name|to
argument_list|)
throw|;
block|}
block|}
else|else
block|{
throw|throw
operator|new
name|GenericFileOperationFailedException
argument_list|(
literal|"Cannot moved existing file from: "
operator|+
name|fileName
operator|+
literal|" to: "
operator|+
name|to
operator|+
literal|" as there already exists a file: "
operator|+
name|to
argument_list|)
throw|;
block|}
block|}
name|LOG
operator|.
name|trace
argument_list|(
literal|"Moving existing file: {} to: {}"
argument_list|,
name|fileName
argument_list|,
name|to
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|operations
operator|.
name|renameFile
argument_list|(
name|fileName
argument_list|,
name|to
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|GenericFileOperationFailedException
argument_list|(
literal|"Cannot rename file from: "
operator|+
name|fileName
operator|+
literal|" to: "
operator|+
name|to
argument_list|)
throw|;
block|}
return|return
literal|true
return|;
block|}
block|}
block|}
end_class

end_unit

