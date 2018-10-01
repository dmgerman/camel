begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|java
operator|.
name|io
operator|.
name|IOException
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
name|Processor
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
name|ResolveEndpointFailedException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|After
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

begin_class
DECL|class|FileConfigureTest
specifier|public
class|class
name|FileConfigureTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|EXPECT_PATH
specifier|private
specifier|static
specifier|final
name|String
name|EXPECT_PATH
init|=
literal|"target"
operator|+
name|File
operator|.
name|separator
operator|+
literal|"foo"
operator|+
name|File
operator|.
name|separator
operator|+
literal|"bar"
decl_stmt|;
DECL|field|EXPECT_FILE
specifier|private
specifier|static
specifier|final
name|String
name|EXPECT_FILE
init|=
literal|"some"
operator|+
name|File
operator|.
name|separator
operator|+
literal|"nested"
operator|+
name|File
operator|.
name|separator
operator|+
literal|"filename.txt"
decl_stmt|;
DECL|field|DUMMY_PROCESSOR
specifier|private
specifier|static
specifier|final
name|Processor
name|DUMMY_PROCESSOR
init|=
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
comment|// Do nothing here
block|}
block|}
decl_stmt|;
annotation|@
name|Test
DECL|method|testUriConfigurations ()
specifier|public
name|void
name|testUriConfigurations
parameter_list|()
throws|throws
name|Exception
block|{
name|assertFileEndpoint
argument_list|(
literal|"file://target/foo/bar"
argument_list|,
name|EXPECT_PATH
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertFileEndpoint
argument_list|(
literal|"file://target/foo/bar?delete=true"
argument_list|,
name|EXPECT_PATH
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertFileEndpoint
argument_list|(
literal|"file:target/foo/bar?delete=true"
argument_list|,
name|EXPECT_PATH
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertFileEndpoint
argument_list|(
literal|"file:target/foo/bar"
argument_list|,
name|EXPECT_PATH
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertFileEndpoint
argument_list|(
literal|"file://target/foo/bar/"
argument_list|,
name|EXPECT_PATH
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertFileEndpoint
argument_list|(
literal|"file://target/foo/bar/?delete=true"
argument_list|,
name|EXPECT_PATH
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertFileEndpoint
argument_list|(
literal|"file:target/foo/bar/?delete=true"
argument_list|,
name|EXPECT_PATH
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertFileEndpoint
argument_list|(
literal|"file:target/foo/bar/"
argument_list|,
name|EXPECT_PATH
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertFileEndpoint
argument_list|(
literal|"file:/target/foo/bar/"
argument_list|,
name|File
operator|.
name|separator
operator|+
name|EXPECT_PATH
operator|+
name|File
operator|.
name|separator
operator|+
name|EXPECT_FILE
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertFileEndpoint
argument_list|(
literal|"file:/"
argument_list|,
name|File
operator|.
name|separator
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertFileEndpoint
argument_list|(
literal|"file:///"
argument_list|,
name|File
operator|.
name|separator
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testUriWithParameters ()
specifier|public
name|void
name|testUriWithParameters
parameter_list|()
throws|throws
name|Exception
block|{
name|FileEndpoint
name|endpoint
init|=
name|resolveMandatoryEndpoint
argument_list|(
literal|"file:///C:/camel/temp?delay=10&useFixedDelay=true&initialDelay=10&consumer.bridgeErrorHandler=true"
operator|+
literal|"&autoCreate=false&startingDirectoryMustExist=true&directoryMustExist=true&readLock=changed"
argument_list|,
name|FileEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Could not find file endpoint"
argument_list|,
name|endpoint
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Get a wrong option of StartingDirectoryMustExist"
argument_list|,
literal|true
argument_list|,
name|endpoint
operator|.
name|isStartingDirectoryMustExist
argument_list|()
argument_list|)
expr_stmt|;
name|endpoint
operator|=
name|resolveMandatoryEndpoint
argument_list|(
literal|"file:///C:/camel/temp?delay=10&useFixedDelay=true&initialDelay=10&startingDirectoryMustExist=true"
operator|+
literal|"&consumer.bridgeErrorHandler=true&autoCreate=false&directoryMustExist=true&readLock=changed"
argument_list|,
name|FileEndpoint
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"Could not find file endpoint"
argument_list|,
name|endpoint
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Get a wrong option of StartingDirectoryMustExist"
argument_list|,
literal|true
argument_list|,
name|endpoint
operator|.
name|isStartingDirectoryMustExist
argument_list|()
argument_list|)
expr_stmt|;
name|endpoint
operator|=
name|resolveMandatoryEndpoint
argument_list|(
literal|"file:///C:/camel/temp?delay=10&startingDirectoryMustExist=true&useFixedDelay=true&initialDelay=10"
operator|+
literal|"&consumer.bridgeErrorHandler=true&autoCreate=false&directoryMustExist=true&readLock=changed"
argument_list|,
name|FileEndpoint
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"Could not find file endpoint"
argument_list|,
name|endpoint
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Get a wrong option of StartingDirectoryMustExist"
argument_list|,
literal|true
argument_list|,
name|endpoint
operator|.
name|isStartingDirectoryMustExist
argument_list|()
argument_list|)
expr_stmt|;
name|endpoint
operator|=
name|resolveMandatoryEndpoint
argument_list|(
literal|"file:///C:/camel/temp?delay=10&useFixedDelay=true&initialDelay=10"
argument_list|,
name|FileEndpoint
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"Could not find file endpoint"
argument_list|,
name|endpoint
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Get a wrong option of StartingDirectoryMustExist"
argument_list|,
literal|false
argument_list|,
name|endpoint
operator|.
name|isStartingDirectoryMustExist
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testUriWithCharset ()
specifier|public
name|void
name|testUriWithCharset
parameter_list|()
throws|throws
name|Exception
block|{
name|FileEndpoint
name|endpoint
init|=
name|resolveMandatoryEndpoint
argument_list|(
literal|"file://target/foo/bar?charset=UTF-8"
argument_list|,
name|FileEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Could not find endpoint: file://target/foo/bar?charset=UTF-8"
argument_list|,
name|endpoint
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Get a wrong charset"
argument_list|,
literal|"UTF-8"
argument_list|,
name|endpoint
operator|.
name|getCharset
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|resolveMandatoryEndpoint
argument_list|(
literal|"file://target/foo/bar?charset=ASSI"
argument_list|,
name|FileEndpoint
operator|.
name|class
argument_list|)
expr_stmt|;
comment|// The charset is wrong
name|fail
argument_list|(
literal|"Expect a configure exception here"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|assertTrue
argument_list|(
literal|"Get the wrong exception type here"
argument_list|,
name|ex
operator|instanceof
name|ResolveEndpointFailedException
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testConsumerConfigurations ()
specifier|public
name|void
name|testConsumerConfigurations
parameter_list|()
throws|throws
name|Exception
block|{
name|FileConsumer
name|consumer
init|=
name|createFileConsumer
argument_list|(
literal|"file://target/foo/bar?recursive=true"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
try|try
block|{
name|createFileConsumer
argument_list|(
literal|"file://target/foo/bar?recursiv=true"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Expect a configure exception here"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|assertTrue
argument_list|(
literal|"Get the wrong exception type here"
argument_list|,
name|ex
operator|instanceof
name|ResolveEndpointFailedException
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
annotation|@
name|After
DECL|method|tearDown ()
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|tearDown
argument_list|()
expr_stmt|;
comment|// one of the above tests created a /target folder in the root we want to get rid of when testing
name|deleteDirectory
argument_list|(
literal|"/target"
argument_list|)
expr_stmt|;
block|}
DECL|method|createFileConsumer (String endpointUri)
specifier|private
name|FileConsumer
name|createFileConsumer
parameter_list|(
name|String
name|endpointUri
parameter_list|)
throws|throws
name|Exception
block|{
name|FileEndpoint
name|endpoint
init|=
name|resolveMandatoryEndpoint
argument_list|(
name|endpointUri
argument_list|,
name|FileEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
return|return
name|endpoint
operator|.
name|createConsumer
argument_list|(
name|DUMMY_PROCESSOR
argument_list|)
return|;
block|}
DECL|method|assertFileEndpoint (String endpointUri, String expectedPath, boolean absolute)
specifier|private
name|void
name|assertFileEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|String
name|expectedPath
parameter_list|,
name|boolean
name|absolute
parameter_list|)
throws|throws
name|IOException
block|{
name|FileEndpoint
name|endpoint
init|=
name|resolveMandatoryEndpoint
argument_list|(
name|endpointUri
argument_list|,
name|FileEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Could not find endpoint: "
operator|+
name|endpointUri
argument_list|,
name|endpoint
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|absolute
condition|)
block|{
name|File
name|file
init|=
name|endpoint
operator|.
name|getFile
argument_list|()
decl_stmt|;
name|String
name|path
init|=
name|file
operator|.
name|getPath
argument_list|()
decl_stmt|;
name|assertDirectoryEquals
argument_list|(
literal|"For uri: "
operator|+
name|endpointUri
operator|+
literal|" the file is not equal"
argument_list|,
name|expectedPath
argument_list|,
name|path
argument_list|)
expr_stmt|;
name|file
operator|=
operator|new
name|File
argument_list|(
name|expectedPath
operator|+
operator|(
name|expectedPath
operator|.
name|endsWith
argument_list|(
name|File
operator|.
name|separator
argument_list|)
condition|?
literal|""
else|:
name|File
operator|.
name|separator
operator|)
operator|+
name|EXPECT_FILE
argument_list|)
expr_stmt|;
name|GenericFile
argument_list|<
name|File
argument_list|>
name|consumedFile
init|=
name|FileConsumer
operator|.
name|asGenericFile
argument_list|(
name|endpoint
operator|.
name|getFile
argument_list|()
operator|.
name|getPath
argument_list|()
argument_list|,
name|file
argument_list|,
literal|null
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|EXPECT_FILE
argument_list|,
name|consumedFile
operator|.
name|getRelativeFilePath
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

