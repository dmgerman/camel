begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.file.remote
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
operator|.
name|remote
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|component
operator|.
name|file
operator|.
name|GenericFile
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
name|GenericFileOperationFailedException
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
name|GenericFileProducer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
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
DECL|class|RemoteFileIgnoreDoPollErrorTest
specifier|public
class|class
name|RemoteFileIgnoreDoPollErrorTest
block|{
DECL|field|remoteFileEndpoint
specifier|private
specifier|final
name|RemoteFileEndpoint
argument_list|<
name|Object
argument_list|>
name|remoteFileEndpoint
init|=
operator|new
name|RemoteFileEndpoint
argument_list|<
name|Object
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|RemoteFileConsumer
argument_list|<
name|Object
argument_list|>
name|buildConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|protected
name|GenericFileProducer
argument_list|<
name|Object
argument_list|>
name|buildProducer
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|RemoteFileOperations
argument_list|<
name|Object
argument_list|>
name|createRemoteFileOperations
parameter_list|()
throws|throws
name|Exception
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getScheme
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
block|}
decl_stmt|;
annotation|@
name|Test
DECL|method|testReadDirErrorIsHandled ()
specifier|public
name|void
name|testReadDirErrorIsHandled
parameter_list|()
throws|throws
name|Exception
block|{
name|RemoteFileConsumer
argument_list|<
name|Object
argument_list|>
name|consumer
init|=
name|getRemoteFileConsumer
argument_list|(
literal|"true"
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|boolean
name|result
init|=
name|consumer
operator|.
name|doSafePollSubDirectory
argument_list|(
literal|"anyPath"
argument_list|,
literal|"adir"
argument_list|,
operator|new
name|ArrayList
argument_list|<
name|GenericFile
argument_list|<
name|Object
argument_list|>
argument_list|>
argument_list|()
argument_list|,
literal|0
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testReadDirErrorIsHandledWithNoMorePoll ()
specifier|public
name|void
name|testReadDirErrorIsHandledWithNoMorePoll
parameter_list|()
throws|throws
name|Exception
block|{
name|RemoteFileConsumer
argument_list|<
name|Object
argument_list|>
name|consumer
init|=
name|getRemoteFileConsumer
argument_list|(
literal|"false"
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|boolean
name|result
init|=
name|consumer
operator|.
name|doSafePollSubDirectory
argument_list|(
literal|"anyPath"
argument_list|,
literal|"adir"
argument_list|,
operator|new
name|ArrayList
argument_list|<
name|GenericFile
argument_list|<
name|Object
argument_list|>
argument_list|>
argument_list|()
argument_list|,
literal|0
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertFalse
argument_list|(
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testReadDirErrorNotHandled ()
specifier|public
name|void
name|testReadDirErrorNotHandled
parameter_list|()
throws|throws
name|Exception
block|{
name|RemoteFileConsumer
argument_list|<
name|Object
argument_list|>
name|consumer
init|=
name|getRemoteFileConsumer
argument_list|(
literal|"IllegalStateException"
argument_list|,
literal|false
argument_list|)
decl_stmt|;
try|try
block|{
name|consumer
operator|.
name|doSafePollSubDirectory
argument_list|(
literal|"anyPath"
argument_list|,
literal|"adir"
argument_list|,
operator|new
name|ArrayList
argument_list|<
name|GenericFile
argument_list|<
name|Object
argument_list|>
argument_list|>
argument_list|()
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|fail
argument_list|(
literal|"Must throw wrapped IllegalStateException in GenericFileOperationFailedException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|GenericFileOperationFailedException
name|e
parameter_list|)
block|{
name|Assert
operator|.
name|assertTrue
argument_list|(
name|e
operator|.
name|getCause
argument_list|()
operator|instanceof
name|IllegalStateException
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testReadDirErrorNotHandledForGenericFileOperationException ()
specifier|public
name|void
name|testReadDirErrorNotHandledForGenericFileOperationException
parameter_list|()
throws|throws
name|Exception
block|{
name|RemoteFileConsumer
argument_list|<
name|Object
argument_list|>
name|consumer
init|=
name|getRemoteFileConsumer
argument_list|(
literal|"GenericFileOperationFailedException"
argument_list|,
literal|false
argument_list|)
decl_stmt|;
try|try
block|{
name|consumer
operator|.
name|doSafePollSubDirectory
argument_list|(
literal|"anyPath"
argument_list|,
literal|"adir"
argument_list|,
operator|new
name|ArrayList
argument_list|<
name|GenericFile
argument_list|<
name|Object
argument_list|>
argument_list|>
argument_list|()
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|fail
argument_list|(
literal|"Must throw GenericFileOperationFailedException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|GenericFileOperationFailedException
name|e
parameter_list|)
block|{
name|Assert
operator|.
name|assertNull
argument_list|(
name|e
operator|.
name|getCause
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getRemoteFileConsumer (final String doPollResult, final boolean ignoreCannotRetrieveFile)
specifier|private
name|RemoteFileConsumer
argument_list|<
name|Object
argument_list|>
name|getRemoteFileConsumer
parameter_list|(
specifier|final
name|String
name|doPollResult
parameter_list|,
specifier|final
name|boolean
name|ignoreCannotRetrieveFile
parameter_list|)
block|{
return|return
operator|new
name|RemoteFileConsumer
argument_list|<
name|Object
argument_list|>
argument_list|(
name|remoteFileEndpoint
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
block|{
annotation|@
name|Override
specifier|protected
name|boolean
name|doPollDirectory
parameter_list|(
name|String
name|absolutePath
parameter_list|,
name|String
name|dirName
parameter_list|,
name|List
argument_list|<
name|GenericFile
argument_list|<
name|Object
argument_list|>
argument_list|>
name|genericFiles
parameter_list|,
name|int
name|depth
parameter_list|)
block|{
switch|switch
condition|(
name|doPollResult
condition|)
block|{
case|case
literal|"IllegalStateException"
case|:
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Problem"
argument_list|)
throw|;
case|case
literal|"GenericFileOperationFailedException"
case|:
throw|throw
operator|new
name|GenericFileOperationFailedException
argument_list|(
literal|"Perm error"
argument_list|)
throw|;
case|case
literal|"true"
case|:
return|return
literal|true
return|;
default|default:
return|return
literal|false
return|;
block|}
block|}
annotation|@
name|Override
specifier|protected
name|boolean
name|pollDirectory
parameter_list|(
name|String
name|fileName
parameter_list|,
name|List
argument_list|<
name|GenericFile
argument_list|<
name|Object
argument_list|>
argument_list|>
name|genericFiles
parameter_list|,
name|int
name|depth
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Override
specifier|protected
name|boolean
name|isMatched
parameter_list|(
name|GenericFile
argument_list|<
name|Object
argument_list|>
name|file
parameter_list|,
name|String
name|doneFileName
parameter_list|,
name|List
argument_list|<
name|Object
argument_list|>
name|files
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Override
specifier|protected
name|boolean
name|ignoreCannotRetrieveFile
parameter_list|(
name|String
name|name
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|Exception
name|cause
parameter_list|)
block|{
return|return
name|ignoreCannotRetrieveFile
return|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

