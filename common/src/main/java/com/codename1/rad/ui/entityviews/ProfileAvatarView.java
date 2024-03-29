/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.rad.ui.entityviews;

import ca.weblite.shared.components.ComponentImage;
import com.codename1.rad.annotations.Inject;
import com.codename1.rad.models.ContentType;

import com.codename1.rad.models.Property;
import com.codename1.rad.models.Tag;
import com.codename1.rad.models.Tags;
import com.codename1.rad.nodes.ActionNode;
import com.codename1.rad.nodes.ActionNode.Category;
import com.codename1.rad.nodes.Node;
import com.codename1.rad.nodes.ViewNode;
import com.codename1.rad.schemas.ListRowItem;
import com.codename1.rad.schemas.Thing;
import com.codename1.rad.ui.AbstractEntityView;
import com.codename1.rad.ui.Actions;
import com.codename1.rad.ui.EntityView;
import com.codename1.rad.ui.UI;
import com.codename1.rad.ui.ViewProperty;
import com.codename1.rad.ui.ViewPropertyParameter;
import com.codename1.rad.ui.image.AsyncImage;
import com.codename1.rad.ui.image.EntityImageRenderer;
import com.codename1.rad.ui.image.FirstCharEntityImageRenderer;
import com.codename1.rad.ui.image.WrappedImage;
import com.codename1.rad.ui.menus.PopupActionsMenu;
import com.codename1.ui.Button;
import com.codename1.ui.CN;
import static com.codename1.ui.CN.NORTH;
import static com.codename1.ui.ComponentSelector.$;
import com.codename1.ui.EncodedImage;
import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import com.codename1.ui.Graphics;
import com.codename1.ui.Image;
import com.codename1.ui.Label;
import com.codename1.ui.URLImage;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.plaf.Border;
import com.codename1.rad.models.Entity;

/**
 * A view that shows a profile's avatar filled in a circle.  
 * 
 * .A sample with 3 ProfileAvatarViews rendered on 3 different entities.  The first entity has a thumbnail.  The 2nd only a name.  The 3rd, neither a name nor thumbnail.
 * image::doc-files/ProfileAvatarView.png[ProfileAvatarView,240]
 * 
 * For best results, the view model (entity) should
 * contain both an "icon" property and a "name" property.  If an "icon" property is defined in the entity type,
 * and the entity contains a valid value for this property (e.g. a URL for an image), then, this image 
 * will be used as the icon.  If the entity doesn't have an icon, but a "name" property is defined and is non-empty
 * in the entity, then the name will be used to create a badge with the first letter of the entity name.  If neither an
 * icon, nor a name can be found on entity, it will simply display the {@link FontImage#MATERIAL_ACCOUNT_CIRCLE} material icon.
 * 
 * 
 * 
 * 
 * === Property Resolution
 * 
 * As mentioned above, the view model should contain both an "icon" and "name" property for best results.  The easiest way
 * to achieve this is to add the {@link #icon} or {@link Thing#thumbnailUrl} tag to the property you wish to use for the icon, and the {@link Thing#name} tag to the
 * property you wish to use for the name.  This view does, however, provide view properties that you can use to customize
 * how the icon and name properties are resolved.
 * 
 * ==== Icon Property Resolution
 * 
 * The Icon property will be resolved in the following order, until it identifies an eligible property on the view model:
 * 
 * . It will check to see if the UI descriptor includes a {@link ViewPropertyParameter} for the  {@link #ICON_PROPERTY} view property, and use its value, if set.
 * . It will check to see if the UI descriptor includes a {@link ViewPropertyParameter} for the {@link #ICON_PROPERTY_TAGS}.  If found, it will use these tags to try to resolve
 * the icon property on the view model.
 * .It will look for a property on the view model tagged with {@link #icon} or {@link Thing#thumbnailUrl} (in that order).  If found, it will use that property.
 * 
 * 
 * ==== Name Property Resolution
 * 
 * The Name property will be resolved in the following order, until it identifies an eligible property on the view model:
 * 
 * 
 * . It will check to see if the UI descriptor includes a {@link ViewPropertyParameter} for the  {@link #NAME_PROPERTY} view property, and use its value, if set.
 * . It will check to see if the UI descriptor includes a {@link ViewPropertyParameter} for the {@link #NAME_PROPERTY_TAGS}.  If found, it will use these tags to try to resolve
 * the name property on the view model.
 * . It will look for a property on the view model tagged with {@link #name}.  If found, it will use that property.
 * 
 * === Supported Actions
 * 
 * The following actions are supported on this view:
 * 
 * . {@link #PROFILE_AVATAR_CLICKED}
 * . {@link #PROFILE_AVATAR_CLICKED_MENU}
 * . {@link #PROFILE_AVATAR_LONG_PRESS}
 * . {@link #PROFILE_AVATAR_LONG_PRESS_MENU}
 * 
 * 
 * == Example
 * 
 * The following example demonstrates the three different rendering strategies imployed by the AvatarProfileView:
 * 
 * 1. Render an image, using the {@link Thing#thumbnailImageUrl}.
 * 2. Render a large letter in a circle with the first letter of the name, using {@link Thing#name}.
 * 3. Render generic icon.
 * 
 * 
 * [source,java]
 * ----
 //require CodeRAD
package com.codename1.samples;



import static com.codename1.ui.CN.*;
import com.codename1.ui.Form;
import com.codename1.ui.Dialog;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;
import com.codename1.io.Log;
import com.codename1.rad.controllers.Controller;
import com.codename1.rad.controllers.ControllerEvent;
import com.codename1.rad.controllers.ViewController;
import com.codename1.ui.Toolbar;
import com.codename1.rad.models.Entity;
import com.codename1.rad.models.EntityType;
import com.codename1.rad.models.StringProperty;
import com.codename1.rad.nodes.ActionNode;
import com.codename1.rad.nodes.ViewNode;
import com.codename1.rad.schemas.Thing;
import static com.codename1.rad.ui.UI.*;
import com.codename1.rad.ui.entityviews.ProfileAvatarView;
import com.codename1.ui.FontImage;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.FlowLayout;

/**
 * This file was generated by <a href="https://www.codenameone.com/">Codename One</a> for the purpose 
 * of building native mobile applications using Java.
 * *{slash}
public class ProfileAvatarViewSample {

    private Form current;
    private Resources theme;

    public void init(Object context) {
        // use two network threads instead of one
        updateNetworkThreadCount(2);

        theme = UIManager.initFirstTheme("/theme");

        // Enable Toolbar on all Forms by default
        Toolbar.setGlobalToolbar(true);

        // Pro only feature
        Log.bindCrashProtection(true);

        addNetworkErrorListener(err -> {
            // prevent the event from propagating
            err.consume();
            if(err.getError() != null) {
                Log.e(err.getError());
            }
            Log.sendLogAsync();
            Dialog.show("Connection Error", "There was a networking error in the connection to " + err.getConnectionRequest().getUrl(), "OK", null);
        });        
    }
    
    public void start() {
        if(current != null){
            current.show();
            return;
        }
        
        
        Form hi = new Form("Hi World", BoxLayout.y());
        
        Profile profile = new Profile(); <1>
        profile.set(Profile.name, "Steve");
        profile.set(Profile.icon, "https://www.codenameone.com/img/steve.jpg");
        
        ProfileAvatarView avatar = new ProfileAvatarView(profile, 30f); <2>
        
        hi.add("Avatar with Name and Icon");
        hi.add(FlowLayout.encloseCenter(avatar));
        
        profile = new Profile();
        profile.set(Profile.name, "Steve");
        
        avatar = new ProfileAvatarView(profile, 30f);
        hi.add("Avatar with only Name");
        hi.add(FlowLayout.encloseCenter(avatar));
        
        profile = new Profile();
        avatar = new ProfileAvatarView(profile, 30f);
        hi.add("Avatar with no name or icon");
        hi.add(FlowLayout.encloseCenter(avatar));
        
        profile = new Profile();
        profile.set(Profile.name, "Steve");
        profile.set(Profile.icon, "https://www.codenameone.com/img/steve.jpg");
        hi.add("Avatar with view controller");
        hi.add(FlowLayout.encloseCenter(new ProfileViewController(null, profile).getView())); <3>
        
        hi.show();
        
        
    }

    public void stop() {
        current = getCurrentForm();
        if(current instanceof Dialog) {
            ((Dialog)current).dispose();
            current = getCurrentForm();
        }
    }
    
    public void destroy() {
    }
    
    public static class Profile extends Entity { <4>
        public static StringProperty name, icon;
        public static final EntityType TYPE = new EntityType() {{
            name = string(tags(Thing.name));
            icon = string(tags(Thing.thumbnailUrl));
            
        }};
    }
    
    
    public static class ProfileViewController extends ViewController { <5>
        private static final ActionNode phone = action( <6>
                icon(FontImage.MATERIAL_PHONE),
                label("Phone")
        );
                
        
        public ProfileViewController(Controller parent, Profile profile) {
            super(parent);
            ViewNode viewNode = new ViewNode(
                    actions(ProfileAvatarView.PROFILE_AVATAR_CLICKED_MENU, phone) <7>
            );
            ProfileAvatarView avatar = new ProfileAvatarView(profile, viewNode, 20f);
            setView(avatar);
            
            addActionListener(phone, evt->{ <8>
                evt.consume();
                Dialog.show("Phone Action clicked", "For user "+evt.getEntity().getText(Thing.name), "OK", null);
            });
        }

        @Override
        public void actionPerformed(ControllerEvent evt) {
            System.out.println("Event "+evt);
        }
        
        
    }

}


 * ----
 * <1> We create a view model for the view.  The ViewModel just has to be an {@link Entity} with properties tagged with {@link Thing#name} and {@link Thing.thumbnailUrl}.
 * <2> We create a `ProfileAvatarView` with the view model we created.
 * <3> To demonstrate actions we wrap a `ProfileAvatarView` inside a {@link ViewController}.  Action events will propagate up the component hierarchy until it finds a 
 * controller that handles it.  This example doesn't use a {@link FormController} or an {@link ApplicationController}, so we use a {@link ViewController} for the particular `ProfileAvatarView`
 * to handle events.
 * <4> The definition of the view model.  A simple `Entity` subclass with two properties.  Notice that we tag properties with {@link Thing#name} and {@link Thing#thumbnailUrl} since
 * these are expected by the view.
 * <5> The definition of the view controller that we use for the last view.  Using a view controller allows us to inject actions in the view and handle their events.
 * <6> We define a "phone" action which will be rendered in the view as a menu item when the avatar is clicked.
 * <7> We register the "phone" action with the {@link #PROFILE_AVATAR_CLICKED_MENU} category so that it will appear in a popup menu when the user clicks it.
 * <8> We register a listener to receive action events from the phone action.
 * 
 * The outcome is
 * 
 * .Running the above sample in the Codename One simulator
 * image::doc-files/ProfileAvatarView.png[]
 * 
 * When the user clicks on the last avatar they receive the following popup menu because we registered an action with the {@link #PROFILE_AVATAR_CLICKED_MENU} category of the view node.
 * 
 * .Popup menu shown when user clicked on avatar because we added an action via the controller.
 * image::doc-files/ProfileAvatarViewClickedMenu.png[]
 * 
 * @author shannah
 */
public class ProfileAvatarView extends AbstractEntityView {

    
    
    
    private Button leadButton = new Button();
    
    /**
     * Default tag to mark the "icon" property in the view model.
     * @see ListRowItem#icon
     */
    public static final Tag icon = ListRowItem.icon;
    
    /**
     * Default tag to mark the "name" property in the view model.
     * @see Thing#name
     */
    public static final Tag name = Thing.name;
    
    private Property iconProp, nameProp;
    private Label label = new Label();
    private float sizeMM = 10f;
    
    public static final ViewProperty<Property> 
            
            /**
             * Optional view property to specify the property to use for the icon.
             */
            ICON_PROPERTY = new ViewProperty(ContentType.createObjectType(Property.class)),
            
            /**
             * Optional view property to specify the property to use for the profile name;
             */
            NAME_PROPERTY = new ViewProperty(ContentType.createObjectType(Property.class));
    
    

    public static final ViewProperty<Tags> 
            
            /**
             * Optional view property to specify the tags to use for the icon property.
             * 
             */
            ICON_PROPERTY_TAGS = new ViewProperty(ContentType.createObjectType(Tags.class)),
            
            /**
             * Optional view property to specify the tags to use for the name property.
             */
            NAME_PROPERTY_TAGS = new ViewProperty(ContentType.createObjectType(Tags.class));

    private static final ViewPropertyParameter
            defaultIconPropertyTags = ViewPropertyParameter.createValueParam(ICON_PROPERTY_TAGS, new Tags(icon, Thing.thumbnailUrl)),
            defaultNamePropertyTags = ViewPropertyParameter.createValueParam(NAME_PROPERTY_TAGS, new Tags(name))
            ;

    /**
     * View property to set the default material icon to a constant.
     */
    public static final ViewProperty<Integer>
        DEFAULT_MATERIAL_ICON = new ViewProperty(ContentType.IntegerType);

    public static final ViewProperty<FallbackSettings>
        FALLBACK_SETTINGS = new ViewProperty(ContentType.createObjectType(FallbackSettings.class));

    private static final ViewPropertyParameter fallbackSettingsParam = ViewPropertyParameter.createValueParam(FALLBACK_SETTINGS, FallbackSettings.FirstChar);


    /**
     * Action that will be fired when the avatar is clicked.
     */
    public static final Category PROFILE_AVATAR_CLICKED = new Category();
    
    /**
     * Action that will be fired when user long presses on the avatar.
     */
    public static final Category PROFILE_AVATAR_LONG_PRESS = new Category();
    
    /**
     * Actions that should appear in a popup menu when the avatar is clicked.  Popup menu
     * will only be displayed if the {@link #PROFILE_AVATAR_CLICKED} action was not consumed.
     */
    public static final Category PROFILE_AVATAR_CLICKED_MENU = new Category();
    
    /**
     * Actions that should appear in a popup menu when the avatar is long pressed.  Popup menu
     * will only be displayed if the {@link #PROFILE_AVATAR_LONG_PRESS} action was not consumed.
     */
    public static final Category PROFILE_AVATAR_LONG_PRESS_MENU = new Category();
    
    private Node node;

    /**
     * The default material icon to use if no icon/image is provided.
     */
    private char defaultMaterialIcon = FontImage.MATERIAL_ACCOUNT_CIRCLE;


    private FallbackSettings fallbackSettings = FallbackSettings.FirstChar;

    /**
     * Enum to set the fallback settings of the avatar profile view, in case there isn't an icon provided.
     * The default is to render the first letter of the name.  And if the name is empty, then to use the icon.
     * Setting fallbackSettings to DefaultIcon will prefer to use the default icon instead of the first letter.
     */
    public static enum FallbackSettings {
        FirstChar,
        DefaultIcon
    }
    
    
    /**
     * Listener attached to the lead button to receive click and long press events.
     * This will convert the event into {@link #PROFILE_AVATAR_CLICKED} or {@link #PROFILE_AVATAR_LONG_PRESS}
     * events.
     */
    private final ActionListener avatarClickedListener = evt->{
        evt.consume();
        if (evt.isLongEvent()) {
            
            ActionNode action = node.getInheritedAction(PROFILE_AVATAR_LONG_PRESS);
            
            if (action != null) {
                ActionEvent ae = action.fireEvent(getEntity(), this);
                if (ae.isConsumed()) {
                    return;
                }
                
            }
            Actions menu = node.getInheritedActions(PROFILE_AVATAR_LONG_PRESS_MENU).getEnabled(getEntity());
            if (!menu.isEmpty()) {
                PopupActionsMenu popupMenu = new PopupActionsMenu(menu, getEntity(), ProfileAvatarView.this);
                popupMenu.showPopupDialog(ProfileAvatarView.this);
            }
            
            
        } else {
            ActionNode action = node.getInheritedAction(PROFILE_AVATAR_CLICKED);
            if (action != null) {
                ActionEvent ae = action.fireEvent(getEntity(), this);
                if (ae.isConsumed()) {
                    return;
                }
            }
            
            Actions menu = node.getInheritedActions(PROFILE_AVATAR_CLICKED_MENU).getEnabled(getEntity());
            if (!menu.isEmpty()) {
                PopupActionsMenu popupMenu = new PopupActionsMenu(menu, getEntity(), ProfileAvatarView.this);
                popupMenu.showPopupDialog(ProfileAvatarView.this);
            }
        }
        
 
    };
    
    /**
     * Creates an avatar for the given profile entity, with the given icon diameter.
     * @param entity The view model.
     * @param sizeMM The size of the avatar icon in mm.
     */
    public ProfileAvatarView(@Inject Entity entity, float sizeMM) {
        this(entity, new ViewNode(), sizeMM);
    }
    
    /**
     * Creats an avatar for the given profile entity, with icon diameter and UI descriptor node.
     * @param entity The view model
     * @param node The ui view descriptor.  This can be used to register actions, view properties, etc... to customize the view.
     * @param sizeMM The size of the avatar icon in mm.
     */
    public ProfileAvatarView(@Inject Entity entity, @Inject Node node, float sizeMM) {
        super(entity);
        setUIID("ProfileAvatarView");
        setGrabsPointerEvents(true);
        setFocusable(true);
        this.sizeMM = sizeMM;
        
        this.node = node;
        
        defaultMaterialIcon = (char)(int)node.getViewParameter(DEFAULT_MATERIAL_ICON, ViewPropertyParameter.createValueParam(DEFAULT_MATERIAL_ICON, (int)FontImage.MATERIAL_ACCOUNT_CIRCLE)).getValue();
        fallbackSettings = (FallbackSettings)node.getViewParameter(FALLBACK_SETTINGS, fallbackSettingsParam).getValue();


        Tags iconTags = (Tags)node.getViewParameter(
                ICON_PROPERTY_TAGS,
                defaultIconPropertyTags
        ).getValue();
        
        Tags nameTags = (Tags)node.getViewParameter(
                NAME_PROPERTY_TAGS,
                defaultNamePropertyTags
        ).getValue();
        
        
        iconProp = (Property)node.getViewParameterValue(ICON_PROPERTY);
        nameProp = (Property)node.getViewParameterValue(NAME_PROPERTY);
        
        
        if (entity != null) {

            if (iconProp == null) {
                iconProp = entity.getEntity().getEntityType().findProperty(iconTags.toArray());
            }
            if (nameProp == null) {
                nameProp = entity.getEntity().getEntityType().findProperty(nameTags.toArray());
            }
        }
        setLayout(new BorderLayout());
        $(label).selectAllStyles().setPadding(0).setMargin(0).setBorder(Border.createEmpty()).setBgTransparency(0x0);
        $(this).selectAllStyles().setPadding(0).setBorder(Border.createEmpty()).setBgTransparency(0x0).setMarginMillimeters(0.5f);
        FontImage.setMaterialIcon(label, defaultMaterialIcon, sizeMM);
        add(CENTER, label);
        setLeadComponent(leadButton);
        leadButton.setHidden(true);
        leadButton.setVisible(false);
        add(NORTH, leadButton);
        
        
        
        update();
        
        
    }
    
    private Object getCircleMask() {
        int size = CN.convertToPixels(sizeMM);
        return UI.getCircleMask(size);
    }

    @Override
    protected Dimension calcPreferredSize() {
        int size = CN.convertToPixels(sizeMM);
        return new Dimension(size, size);
    }
    
    private boolean iconLoaded;

    @Override
    public void update() {
        if (iconLoaded) {
            return;
        }
        iconLoaded = true;
        if (getEntity() != null) {
            

            if (iconProp != null && !getEntity().isEmpty(iconProp)) {
                int sizePx = CN.convertToPixels(sizeMM);
                Image img = getEntity().getEntity().createImageToStorage(iconProp, 
                        EncodedImage.createFromImage(label.getIcon().fill(sizePx, sizePx), false), 
                        "@avatar"+label.getIcon().getWidth()+"x"+label.getIcon().getHeight(), 
                        URLImage.createMaskAdapter(getCircleMask()));
                if (img != null) {
                    label.setIcon(img);
                    return;
                }
                
            }

            if (fallbackSettings == FallbackSettings.FirstChar && nameProp != null && !getEntity().isEmpty(nameProp)) {
                FirstCharEntityImageRenderer renderer = new FirstCharEntityImageRenderer(sizeMM);

                AsyncImage img = renderer.createImage(this, nameProp, 0, false, false);
                if (img != null) {
                    img.ready(im->{
                        label.setIcon(im);
                        Form f = label.getComponentForm();
                        if (f != null) {
                            label.repaint();
                            
                        }
                    });
                    return;
                }

            }
            FontImage.setMaterialIcon(label, defaultMaterialIcon, sizeMM);
            
        } else {
            FontImage.setMaterialIcon(label, defaultMaterialIcon, sizeMM);
        }
    }
    
    /**
     * Sets the this view as the icon for the given label.
     * @param label 
     */
    public void setIcon(Label label) {
        int sizePx = CN.convertToPixels(sizeMM);
        setWidth(sizePx);
        setHeight(sizePx);
        layoutContainer();
        label.setIcon(new ComponentImage(this, sizePx, sizePx));
    }

    @Override
    public void commit() {
        
    }

    @Override
    public Node getViewNode() {
        return node;
    }

    /**
     * An image renderer that will render a {@link ProfileAvatarView} as an image for the given entity.
     */
    public static class ImageRenderer implements EntityImageRenderer {

        private float sizeMM = 10f;
        
        public ImageRenderer(float sizeMM) {
            this.sizeMM = sizeMM;
        }
        
        @Override
        public AsyncImage createImage(EntityView view, Property property, int rowIndex, boolean selected, boolean focused) {
            int size = CN.convertToPixels(sizeMM);
            return new WrappedImage(new ComponentImage(new ProfileAvatarView(view.getEntity(), view.getViewNode(), sizeMM), size, size));
        }
        
    }

    @Override
    protected void bindImpl() {
        leadButton.addActionListener(avatarClickedListener);
        leadButton.addLongPressListener(avatarClickedListener);
    }

    @Override
    protected void unbindImpl() {
        leadButton.removeActionListener(avatarClickedListener);
        leadButton.removeLongPressListener(avatarClickedListener);

    }
    
    
    
    
    
}
