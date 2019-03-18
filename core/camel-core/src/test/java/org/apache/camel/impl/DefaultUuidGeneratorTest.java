begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|StopWatch
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
name|TimeUtils
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

begin_class
DECL|class|DefaultUuidGeneratorTest
specifier|public
class|class
name|DefaultUuidGeneratorTest
extends|extends
name|Assert
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
name|DefaultUuidGeneratorTest
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|Test
DECL|method|testGenerateUUID ()
specifier|public
name|void
name|testGenerateUUID
parameter_list|()
block|{
name|DefaultUuidGenerator
name|uuidGenerator
init|=
operator|new
name|DefaultUuidGenerator
argument_list|()
decl_stmt|;
name|String
name|firstUUID
init|=
name|uuidGenerator
operator|.
name|generateUuid
argument_list|()
decl_stmt|;
name|String
name|secondUUID
init|=
name|uuidGenerator
operator|.
name|generateUuid
argument_list|()
decl_stmt|;
name|assertNotSame
argument_list|(
name|firstUUID
argument_list|,
name|secondUUID
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testPerformance ()
specifier|public
name|void
name|testPerformance
parameter_list|()
block|{
name|DefaultUuidGenerator
name|uuidGenerator
init|=
operator|new
name|DefaultUuidGenerator
argument_list|()
decl_stmt|;
name|StopWatch
name|watch
init|=
operator|new
name|StopWatch
argument_list|()
decl_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"First id: "
operator|+
name|uuidGenerator
operator|.
name|generateUuid
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|500000
condition|;
name|i
operator|++
control|)
block|{
name|uuidGenerator
operator|.
name|generateUuid
argument_list|()
expr_stmt|;
block|}
name|LOG
operator|.
name|info
argument_list|(
literal|"Last id:  "
operator|+
name|uuidGenerator
operator|.
name|generateUuid
argument_list|()
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Took "
operator|+
name|TimeUtils
operator|.
name|printDuration
argument_list|(
name|watch
operator|.
name|taken
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSanitizeHostName ()
specifier|public
name|void
name|testSanitizeHostName
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
literal|"somehost.lan"
argument_list|,
name|DefaultUuidGenerator
operator|.
name|sanitizeHostName
argument_list|(
literal|"somehost.lan"
argument_list|)
argument_list|)
expr_stmt|;
comment|// include a UTF-8 char in the text \u0E08 is a Thai elephant
name|assertEquals
argument_list|(
literal|"otherhost.lan"
argument_list|,
name|DefaultUuidGenerator
operator|.
name|sanitizeHostName
argument_list|(
literal|"other\u0E08host.lan"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

