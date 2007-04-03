begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_package
DECL|package|org.apache.camel.component.jpa
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jpa
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|Retention
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|RetentionPolicy
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|Target
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|ElementType
import|;
end_import

begin_comment
comment|/**  * An annotation to mark a method to be invoked when an entity bean has been succesfully processed  * by a Camel consumer, so that it can be updated in some way to remove it from the query set.  *  * For example a method may be marked to set an active flag to false or to update some status value to the next step in a workflow  *  * @version $Revision$  */
end_comment

begin_annotation_defn
annotation|@
name|Retention
argument_list|(
name|RetentionPolicy
operator|.
name|RUNTIME
argument_list|)
annotation|@
name|Target
argument_list|(
block|{
name|ElementType
operator|.
name|METHOD
block|}
argument_list|)
DECL|annotation|Consumed
specifier|public
annotation_defn|@interface
name|Consumed
block|{ }
end_annotation_defn

end_unit

