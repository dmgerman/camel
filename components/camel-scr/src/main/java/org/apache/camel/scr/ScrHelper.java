begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.scr
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|scr
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|namespace
operator|.
name|QName
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|XMLEventReader
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|XMLInputFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|XMLStreamConstants
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|events
operator|.
name|XMLEvent
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
comment|/**  * Helper class for reading properties from a component description file. Used in unit testing.  */
end_comment

begin_class
DECL|class|ScrHelper
specifier|public
specifier|final
class|class
name|ScrHelper
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
name|ScrHelper
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|ScrHelper ()
specifier|private
name|ScrHelper
parameter_list|()
block|{     }
DECL|method|getScrProperties (String componentName)
specifier|public
specifier|static
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getScrProperties
parameter_list|(
name|String
name|componentName
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|getScrProperties
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"target/classes/OSGI-INF/%s.xml"
argument_list|,
name|componentName
argument_list|)
argument_list|,
name|componentName
argument_list|)
return|;
block|}
DECL|method|getScrProperties (String xmlLocation, String componentName)
specifier|public
specifier|static
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getScrProperties
parameter_list|(
name|String
name|xmlLocation
parameter_list|,
name|String
name|componentName
parameter_list|)
throws|throws
name|Exception
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|result
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|XMLInputFactory
name|inputFactory
init|=
name|XMLInputFactory
operator|.
name|newFactory
argument_list|()
decl_stmt|;
name|inputFactory
operator|.
name|setProperty
argument_list|(
name|XMLInputFactory
operator|.
name|IS_NAMESPACE_AWARE
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|XMLEventReader
name|eventReader
init|=
name|inputFactory
operator|.
name|createXMLEventReader
argument_list|(
operator|new
name|FileReader
argument_list|(
name|xmlLocation
argument_list|)
argument_list|)
decl_stmt|;
name|boolean
name|collect
init|=
literal|false
decl_stmt|;
while|while
condition|(
name|eventReader
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|XMLEvent
name|event
init|=
name|eventReader
operator|.
name|nextEvent
argument_list|()
decl_stmt|;
if|if
condition|(
name|event
operator|.
name|getEventType
argument_list|()
operator|==
name|XMLStreamConstants
operator|.
name|START_ELEMENT
operator|&&
name|event
operator|.
name|asStartElement
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|toString
argument_list|()
operator|.
name|equals
argument_list|(
literal|"scr:component"
argument_list|)
operator|&&
name|event
operator|.
name|asStartElement
argument_list|()
operator|.
name|getAttributeByName
argument_list|(
name|QName
operator|.
name|valueOf
argument_list|(
literal|"name"
argument_list|)
argument_list|)
operator|.
name|getValue
argument_list|()
operator|.
name|equals
argument_list|(
name|componentName
argument_list|)
condition|)
block|{
name|collect
operator|=
literal|true
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|collect
operator|&&
name|event
operator|.
name|getEventType
argument_list|()
operator|==
name|XMLStreamConstants
operator|.
name|START_ELEMENT
operator|&&
name|event
operator|.
name|asStartElement
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|toString
argument_list|()
operator|.
name|equals
argument_list|(
literal|"property"
argument_list|)
condition|)
block|{
name|result
operator|.
name|put
argument_list|(
name|event
operator|.
name|asStartElement
argument_list|()
operator|.
name|getAttributeByName
argument_list|(
name|QName
operator|.
name|valueOf
argument_list|(
literal|"name"
argument_list|)
argument_list|)
operator|.
name|getValue
argument_list|()
argument_list|,
name|event
operator|.
name|asStartElement
argument_list|()
operator|.
name|getAttributeByName
argument_list|(
name|QName
operator|.
name|valueOf
argument_list|(
literal|"value"
argument_list|)
argument_list|)
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|collect
operator|&&
name|event
operator|.
name|getEventType
argument_list|()
operator|==
name|XMLStreamConstants
operator|.
name|END_ELEMENT
operator|&&
name|event
operator|.
name|asEndElement
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|toString
argument_list|()
operator|.
name|equals
argument_list|(
literal|"scr:component"
argument_list|)
condition|)
block|{
break|break;
block|}
block|}
return|return
name|result
return|;
block|}
block|}
end_class

end_unit

