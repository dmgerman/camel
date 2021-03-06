begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.leveldb
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|leveldb
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
name|test
operator|.
name|junit4
operator|.
name|CamelTestSupport
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
DECL|class|LevelDBSetupTest
specifier|public
class|class
name|LevelDBSetupTest
extends|extends
name|CamelTestSupport
block|{
comment|/**      * The Level db file.      */
DECL|field|levelDBFile
specifier|private
name|LevelDBFile
name|levelDBFile
decl_stmt|;
annotation|@
name|Override
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|tearDown ()
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|deleteDirectory
argument_list|(
literal|"leveldb.dat"
argument_list|)
expr_stmt|;
name|super
operator|.
name|tearDown
argument_list|()
expr_stmt|;
block|}
comment|/**      * Test level db start with no path.      */
annotation|@
name|Test
DECL|method|testLevelDBStartWithNoPath ()
specifier|public
name|void
name|testLevelDBStartWithNoPath
parameter_list|()
block|{
name|levelDBFile
operator|=
operator|new
name|LevelDBFile
argument_list|()
expr_stmt|;
name|levelDBFile
operator|.
name|setFileName
argument_list|(
literal|"leveldb.dat"
argument_list|)
expr_stmt|;
name|levelDBFile
operator|.
name|start
argument_list|()
expr_stmt|;
name|levelDBFile
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
comment|/**      * Test level db start with path.      */
annotation|@
name|Test
DECL|method|testLevelDBStartWithPath ()
specifier|public
name|void
name|testLevelDBStartWithPath
parameter_list|()
block|{
name|deleteDirectory
argument_list|(
literal|"target/data"
argument_list|)
expr_stmt|;
name|levelDBFile
operator|=
operator|new
name|LevelDBFile
argument_list|()
expr_stmt|;
name|levelDBFile
operator|.
name|setFileName
argument_list|(
literal|"target/data/leveldb.dat"
argument_list|)
expr_stmt|;
name|levelDBFile
operator|.
name|start
argument_list|()
expr_stmt|;
name|levelDBFile
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

