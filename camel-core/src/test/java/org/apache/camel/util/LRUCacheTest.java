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
name|junit
operator|.
name|framework
operator|.
name|TestCase
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
name|Service
import|;
end_import

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|LRUCacheTest
specifier|public
class|class
name|LRUCacheTest
extends|extends
name|TestCase
block|{
DECL|field|cache
specifier|private
name|LRUCache
argument_list|<
name|String
argument_list|,
name|Service
argument_list|>
name|cache
decl_stmt|;
annotation|@
name|Override
DECL|method|setUp ()
specifier|protected
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|cache
operator|=
operator|new
name|LRUCache
argument_list|<
name|String
argument_list|,
name|Service
argument_list|>
argument_list|(
literal|10
argument_list|)
expr_stmt|;
block|}
DECL|method|testLRUCache ()
specifier|public
name|void
name|testLRUCache
parameter_list|()
block|{
name|MyService
name|service1
init|=
operator|new
name|MyService
argument_list|()
decl_stmt|;
name|MyService
name|service2
init|=
operator|new
name|MyService
argument_list|()
decl_stmt|;
name|cache
operator|.
name|put
argument_list|(
literal|"A"
argument_list|,
name|service1
argument_list|)
expr_stmt|;
name|cache
operator|.
name|put
argument_list|(
literal|"B"
argument_list|,
name|service2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|cache
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|service1
argument_list|,
name|cache
operator|.
name|get
argument_list|(
literal|"A"
argument_list|)
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|service2
argument_list|,
name|cache
operator|.
name|get
argument_list|(
literal|"B"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testLRUCacheEviction ()
specifier|public
name|void
name|testLRUCacheEviction
parameter_list|()
block|{
name|MyService
name|service1
init|=
operator|new
name|MyService
argument_list|()
decl_stmt|;
name|MyService
name|service2
init|=
operator|new
name|MyService
argument_list|()
decl_stmt|;
name|MyService
name|service3
init|=
operator|new
name|MyService
argument_list|()
decl_stmt|;
name|MyService
name|service4
init|=
operator|new
name|MyService
argument_list|()
decl_stmt|;
name|MyService
name|service5
init|=
operator|new
name|MyService
argument_list|()
decl_stmt|;
name|MyService
name|service6
init|=
operator|new
name|MyService
argument_list|()
decl_stmt|;
name|MyService
name|service7
init|=
operator|new
name|MyService
argument_list|()
decl_stmt|;
name|MyService
name|service8
init|=
operator|new
name|MyService
argument_list|()
decl_stmt|;
name|MyService
name|service9
init|=
operator|new
name|MyService
argument_list|()
decl_stmt|;
name|MyService
name|service10
init|=
operator|new
name|MyService
argument_list|()
decl_stmt|;
name|MyService
name|service11
init|=
operator|new
name|MyService
argument_list|()
decl_stmt|;
name|MyService
name|service12
init|=
operator|new
name|MyService
argument_list|()
decl_stmt|;
name|cache
operator|.
name|put
argument_list|(
literal|"A"
argument_list|,
name|service1
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|service1
operator|.
name|getStopped
argument_list|()
argument_list|)
expr_stmt|;
name|cache
operator|.
name|put
argument_list|(
literal|"B"
argument_list|,
name|service2
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|service2
operator|.
name|getStopped
argument_list|()
argument_list|)
expr_stmt|;
name|cache
operator|.
name|put
argument_list|(
literal|"C"
argument_list|,
name|service3
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|service3
operator|.
name|getStopped
argument_list|()
argument_list|)
expr_stmt|;
name|cache
operator|.
name|put
argument_list|(
literal|"D"
argument_list|,
name|service4
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|service4
operator|.
name|getStopped
argument_list|()
argument_list|)
expr_stmt|;
name|cache
operator|.
name|put
argument_list|(
literal|"E"
argument_list|,
name|service5
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|service5
operator|.
name|getStopped
argument_list|()
argument_list|)
expr_stmt|;
name|cache
operator|.
name|put
argument_list|(
literal|"F"
argument_list|,
name|service6
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|service6
operator|.
name|getStopped
argument_list|()
argument_list|)
expr_stmt|;
name|cache
operator|.
name|put
argument_list|(
literal|"G"
argument_list|,
name|service7
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|service7
operator|.
name|getStopped
argument_list|()
argument_list|)
expr_stmt|;
name|cache
operator|.
name|put
argument_list|(
literal|"H"
argument_list|,
name|service8
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|service8
operator|.
name|getStopped
argument_list|()
argument_list|)
expr_stmt|;
name|cache
operator|.
name|put
argument_list|(
literal|"I"
argument_list|,
name|service9
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|service9
operator|.
name|getStopped
argument_list|()
argument_list|)
expr_stmt|;
name|cache
operator|.
name|put
argument_list|(
literal|"J"
argument_list|,
name|service10
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|service10
operator|.
name|getStopped
argument_list|()
argument_list|)
expr_stmt|;
comment|// we are now full
name|assertEquals
argument_list|(
literal|10
argument_list|,
name|cache
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|cache
operator|.
name|put
argument_list|(
literal|"K"
argument_list|,
name|service11
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|service11
operator|.
name|getStopped
argument_list|()
argument_list|)
expr_stmt|;
comment|// should evict the eldest, and stop the service
name|assertTrue
argument_list|(
name|service1
operator|.
name|getStopped
argument_list|()
argument_list|)
expr_stmt|;
name|cache
operator|.
name|put
argument_list|(
literal|"L"
argument_list|,
name|service12
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|service12
operator|.
name|getStopped
argument_list|()
argument_list|)
expr_stmt|;
comment|// should evict the eldest, and stop the service
name|assertTrue
argument_list|(
name|service2
operator|.
name|getStopped
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|10
argument_list|,
name|cache
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testLRUCacheHitsAndMisses ()
specifier|public
name|void
name|testLRUCacheHitsAndMisses
parameter_list|()
block|{
name|MyService
name|service1
init|=
operator|new
name|MyService
argument_list|()
decl_stmt|;
name|MyService
name|service2
init|=
operator|new
name|MyService
argument_list|()
decl_stmt|;
name|cache
operator|.
name|put
argument_list|(
literal|"A"
argument_list|,
name|service1
argument_list|)
expr_stmt|;
name|cache
operator|.
name|put
argument_list|(
literal|"B"
argument_list|,
name|service2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|cache
operator|.
name|getHits
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|cache
operator|.
name|getMisses
argument_list|()
argument_list|)
expr_stmt|;
name|cache
operator|.
name|get
argument_list|(
literal|"A"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|cache
operator|.
name|getHits
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|cache
operator|.
name|getMisses
argument_list|()
argument_list|)
expr_stmt|;
name|cache
operator|.
name|get
argument_list|(
literal|"A"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|cache
operator|.
name|getHits
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|cache
operator|.
name|getMisses
argument_list|()
argument_list|)
expr_stmt|;
name|cache
operator|.
name|get
argument_list|(
literal|"B"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|cache
operator|.
name|getHits
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|cache
operator|.
name|getMisses
argument_list|()
argument_list|)
expr_stmt|;
name|cache
operator|.
name|get
argument_list|(
literal|"C"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|cache
operator|.
name|getHits
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|cache
operator|.
name|getMisses
argument_list|()
argument_list|)
expr_stmt|;
name|cache
operator|.
name|get
argument_list|(
literal|"D"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|cache
operator|.
name|getHits
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|cache
operator|.
name|getMisses
argument_list|()
argument_list|)
expr_stmt|;
name|cache
operator|.
name|resetStatistics
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|cache
operator|.
name|getHits
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|cache
operator|.
name|getMisses
argument_list|()
argument_list|)
expr_stmt|;
name|cache
operator|.
name|get
argument_list|(
literal|"B"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|cache
operator|.
name|getHits
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|cache
operator|.
name|getMisses
argument_list|()
argument_list|)
expr_stmt|;
name|cache
operator|.
name|clear
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|cache
operator|.
name|getHits
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|cache
operator|.
name|getMisses
argument_list|()
argument_list|)
expr_stmt|;
name|cache
operator|.
name|get
argument_list|(
literal|"B"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|cache
operator|.
name|getHits
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|cache
operator|.
name|getMisses
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|class|MyService
specifier|private
specifier|static
specifier|final
class|class
name|MyService
implements|implements
name|Service
block|{
DECL|field|stopped
specifier|private
name|Boolean
name|stopped
decl_stmt|;
DECL|method|start ()
specifier|public
name|void
name|start
parameter_list|()
throws|throws
name|Exception
block|{         }
DECL|method|stop ()
specifier|public
name|void
name|stop
parameter_list|()
throws|throws
name|Exception
block|{
name|stopped
operator|=
literal|true
expr_stmt|;
block|}
DECL|method|getStopped ()
specifier|public
name|Boolean
name|getStopped
parameter_list|()
block|{
return|return
name|stopped
return|;
block|}
block|}
block|}
end_class

end_unit

