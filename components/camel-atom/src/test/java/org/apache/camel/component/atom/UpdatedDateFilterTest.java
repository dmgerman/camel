begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.atom
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|atom
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Calendar
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
name|java
operator|.
name|util
operator|.
name|TimeZone
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|abdera
operator|.
name|model
operator|.
name|Document
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|abdera
operator|.
name|model
operator|.
name|Entry
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|abdera
operator|.
name|model
operator|.
name|Feed
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
name|feed
operator|.
name|EntryFilter
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

begin_comment
comment|/**  * Unit test for UpdatedDateFilter  */
end_comment

begin_class
DECL|class|UpdatedDateFilterTest
specifier|public
class|class
name|UpdatedDateFilterTest
extends|extends
name|Assert
block|{
annotation|@
name|Test
DECL|method|testFilter ()
specifier|public
name|void
name|testFilter
parameter_list|()
throws|throws
name|Exception
block|{
name|Document
argument_list|<
name|Feed
argument_list|>
name|doc
init|=
name|AtomUtils
operator|.
name|parseDocument
argument_list|(
literal|"file:src/test/data/feed.atom"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|doc
argument_list|)
expr_stmt|;
comment|// timestamp from the feed to use as base
comment|// 2007-11-13T13:35:25.014Z
name|Calendar
name|cal
init|=
name|Calendar
operator|.
name|getInstance
argument_list|(
name|TimeZone
operator|.
name|getTimeZone
argument_list|(
literal|"GMT+1:00"
argument_list|)
argument_list|)
decl_stmt|;
name|cal
operator|.
name|set
argument_list|(
literal|2007
argument_list|,
name|Calendar
operator|.
name|NOVEMBER
argument_list|,
literal|13
argument_list|,
literal|14
argument_list|,
literal|35
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|EntryFilter
name|filter
init|=
operator|new
name|UpdatedDateFilter
argument_list|(
name|cal
operator|.
name|getTime
argument_list|()
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Entry
argument_list|>
name|entries
init|=
name|doc
operator|.
name|getRoot
argument_list|()
operator|.
name|getEntries
argument_list|()
decl_stmt|;
comment|// must reverse backwards
for|for
control|(
name|int
name|i
init|=
name|entries
operator|.
name|size
argument_list|()
operator|-
literal|1
init|;
name|i
operator|>
literal|0
condition|;
name|i
operator|--
control|)
block|{
name|Entry
name|entry
init|=
name|entries
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|boolean
name|valid
init|=
name|filter
operator|.
name|isValidEntry
argument_list|(
literal|null
argument_list|,
name|doc
argument_list|,
name|entry
argument_list|)
decl_stmt|;
comment|// only the 3 last should be true
if|if
condition|(
name|i
operator|>
literal|3
condition|)
block|{
name|assertEquals
argument_list|(
literal|"not valid"
argument_list|,
literal|false
argument_list|,
name|valid
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|assertEquals
argument_list|(
literal|"valid"
argument_list|,
literal|true
argument_list|,
name|valid
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

